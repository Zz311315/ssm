package com.by.service.impl;

import com.by.dao.TPhoneDao;
import com.by.entity.TPhone;
import com.by.service.TPhoneService;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * (TPhone)表服务实现类
 *
 * @author makejava
 * @since 2019-11-22 15:30:24
 */
@Service("tPhoneService")
public class TPhoneServiceImpl implements TPhoneService {
    @Resource
    private TPhoneDao tPhoneDao;
    @Autowired
    private SolrClient solrClient;
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;



    /**
     * 通过ID查询单条数据
     *
     * @param phoneId 主键
     * @return 实例对象
     */


    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<TPhone> queryAllByLimit(int offset, int limit) {
        List<TPhone> tPhones = tPhoneDao.queryAllByLimit(offset, limit);
        try {
            solrClient.addBeans(tPhones);
            solrClient.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tPhones;
    }



    @Override
    public List<TPhone> sl(String mmm, int i, Integer limit) throws IOException, SolrServerException {
        List<TPhone> beans=null;
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("search_key:"+mmm);
        solrQuery.add("q.op","and");

        //开启高亮
        solrQuery.setHighlight(true);
        solrQuery.setHighlightSimplePre("<font color='red'>");//前缀
        solrQuery.setHighlightSimplePost("</font>");//后缀
        //设置高亮字段
        solrQuery.addHighlightField("phone_name");
        solrQuery.addHighlightField("phone_inf");
        solrQuery.addHighlightField("phone_cd");
        solrQuery.setStart(i);
        solrQuery.setRows(limit);
        QueryResponse query = solrClient.query(solrQuery);
        //获取Bean对象
        beans= query.getBeans(TPhone.class);
        System.out.println(beans.size());
        //获取高亮数据
        Map<String, Map<String, List<String>>> map = query.getHighlighting();
        System.out.println(map);
        for (TPhone bean : beans) {
            Integer phoneId = bean.getPhoneId();
            if (map.get(phoneId+"")!=null&&map.get(phoneId+"").get("phone_name")!=null){
                bean.setPhoneName(map.get(phoneId+"").get("phone_name").get(0));
            }if (map.get(phoneId+"")!=null&&map.get(phoneId+"").get("phone_inf")!=null){
                bean.setPhoneInf(map.get(phoneId+"").get("phone_inf").get(0));
            }if (map.get(phoneId+"")!=null&&map.get(phoneId+"").get("phone_cd")!=null){
                bean.setPhoneCd(map.get(phoneId+"").get("phone_cd").get(0));
            }
        }
        return beans;
    }




    @Override
    public void delete(Integer id) {
        ActiveMQQueue boot_queue = new ActiveMQQueue("boot_queue1");
        jmsMessagingTemplate.convertAndSend(boot_queue,id);
        tPhoneDao.delete(id);
    }
    @JmsListener(destination = "boot_queue1")
    public void receive1(Integer id){
        try {
            System.out.println(id);
            solrClient.deleteById(String.valueOf(id));
            solrClient.commit();
        }  catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<TPhone> find() {
        return tPhoneDao.find();
    }


    /**
     * 新增数据
     *
     * @param tPhone 实例对象
     * @return 实例对象
     */

    @Override
    public TPhone insert(TPhone tPhone) {
        tPhoneDao.insert(tPhone);
        ActiveMQQueue boot_queue = new ActiveMQQueue("boot_queue");
        jmsMessagingTemplate.convertAndSend(boot_queue,tPhone);
        return tPhone;
    }
    @JmsListener(destination = "boot_queue")
    public void receive(Message textMessage){
        try {
            ActiveMQObjectMessage activeMQObjectMessage= (ActiveMQObjectMessage)textMessage;
            TPhone object = (TPhone) activeMQObjectMessage.getObject();
            System.out.println(object);
            solrClient.addBean(object);
            solrClient.commit();
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






}
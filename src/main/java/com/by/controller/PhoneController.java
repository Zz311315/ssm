package com.by.controller;

import com.by.entity.TPhone;
import com.by.service.TPhoneService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("phone")
public class PhoneController {
    @Autowired
    private TPhoneService tPhoneService;
    @Autowired
    private SolrClient solrClient;
    @RequestMapping("list")
    public Map<String, Object> list(Integer page,Integer limit){
        List<TPhone> list=tPhoneService.queryAllByLimit((page-1)*limit,limit);
        Map<String,Object> map = new HashMap<>();
        map.put("code",0);
        map.put("msg","");
        map.put("data",list);
        return map;
    }









    @RequestMapping("sl")
    public Map<String, Object> sl(String mmm, Integer page, Integer limit){
        List<TPhone> list= null;
        List<TPhone> list1=null;

        if(mmm!=null){
            try {
                //solr库查询的数据
                list = tPhoneService.sl(mmm,(page - 1) * limit, limit);
                SolrQuery solrQuery = new SolrQuery();
                solrQuery.setQuery("search_key:"+mmm);
                solrQuery.add("q.op","and");
                solrQuery.setRows(100);
                QueryResponse query = solrClient.query(solrQuery);
                List<TPhone> beans = query.getBeans(TPhone.class);

                list1=beans;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SolrServerException e) {
                e.printStackTrace();
            }
        }else {
            list = tPhoneService.queryAllByLimit((page - 1) * limit, limit);
            //数据库的条数
            list1=tPhoneService.find();
        }
        Map<String,Object> map = new HashMap<>();
        map.put("code",0);
        map.put("msg","");
        map.put("count",list1.size());
        map.put("data",list);
        return map;
    }








    @RequestMapping("add")
    public Map<String, Object> add(TPhone tPhone){
        Map<String, Object> map = new HashMap<>();
        try {
            tPhoneService.insert(tPhone);
            map.put("success", true);

        } catch (Exception e) {
            map.put("error", false);
            e.printStackTrace();
        }
        return map;
    }

    @RequestMapping("/phone{id}")
    public Map<String, Object> delete(@PathVariable("id") Integer id){
        Map<String, Object> map = new HashMap<>();
        try {
            tPhoneService.delete(id);
            map.put("success", true);

        } catch (Exception e) {
            map.put("error", false);
            e.printStackTrace();
        }
        return map;


    }





}

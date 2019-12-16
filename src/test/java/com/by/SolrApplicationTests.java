package com.by;

import com.by.dao.TPhoneDao;
import com.by.entity.TPhone;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@SpringBootTest
class SolrApplicationTests {
    @Autowired
    private SolrClient solrClient;
    @Autowired
    private TPhoneDao tPhoneDao;
    @Test
    void contextLoads() throws IOException, SolrServerException {

        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("search_key:苹果");
        solrQuery.setHighlight(true);
        solrQuery.setHighlightSimplePost("<font color='red'>");    //前缀
        solrQuery.setHighlightSimplePre("</font>");    //后缀
        solrQuery.addHighlightField("phone_name");
        solrQuery.addHighlightField("phone_inf");
        solrQuery.addHighlightField("phone_cd");//开启高亮
        QueryResponse query = solrClient.query(solrQuery);
        List<TPhone> beans = query.getBeans(TPhone.class);
        Map<String, Map<String, List<String>>> map = query.getHighlighting();
        System.out.println(map);
    }
    @Test
    public  void test() throws IOException, SolrServerException {

        List<TPhone> tPhones = tPhoneDao.queryAllByLimit(0, 10000);
        solrClient.addBeans(tPhones);
        solrClient.commit();
    }
}

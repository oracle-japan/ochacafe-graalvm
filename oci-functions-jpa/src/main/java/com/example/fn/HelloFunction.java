package com.example.fn;


import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import com.fnproject.fn.api.FnConfiguration;
import com.fnproject.fn.api.RuntimeContext;

public class HelloFunction {

    private static Logger logger = Logger.getLogger(HelloFunction.class.getName());

    public EntityManagerFactory emf;

    @FnConfiguration
    public void setUp(RuntimeContext rctx) {

        java.security.Security.setProperty("networkaddress.cache.ttl" , "0");

        try{
            final Map<String, String> config = rctx.getConfiguration();
            //System.out.println(config);
            //System.getenv().entrySet().forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));
            
            // OCI Secret の取得
            OciSecrets ociSecrets = new OciSecrets();
            String region = config.get("OCI_REGION");
            String password = config.get("JDBC_PASSWORD");
            if(Objects.isNull(password)){
                password = ociSecrets.getSecretById(region, config.get("JDBC_PASSWORD_SECRET_ID"));
                logger.fine("Password was retrieved from Secret successfully.");
            }

            // wallet file の取得
            String walletDir = config.get("ADB_WALLET_DIR");
            if(Objects.nonNull(walletDir)){
                String adbId = config.get("ADB_ID");
                OciDatabase ociDatabase = new OciDatabase();
                ociDatabase.downloadWallet(adbId, walletDir, region);

                //System.setProperty("oracle.net.tns_admin", walletDir);
                //System.setProperty("oracle.net.wallet_location",
    			//	"(SOURCE=(METHOD=FILE)(METHOD_DATA=(DIRECTORY=" + walletDir + ")))");
            }

            // JPA EntityManagerFactory の取得
            final Map<String, String> props = new HashMap<>();
            props.put("jakarta.persistence.jdbc.driver", config.get("JDBC_DRIVER"));
            props.put("jakarta.persistence.jdbc.url", config.get("JDBC_URL"));
            props.put("jakarta.persistence.jdbc.user", config.get("JDBC_USER"));
            props.put("jakarta.persistence.jdbc.password", password);
    
            emf = Persistence.createEntityManagerFactory("demo", props);

        }catch(Exception e){
            throw new RuntimeException("An error occured while @FnConfiguration - " + e.getMessage(), e);
        }

    }

    /**
     * 国コード(int)を受け取ってDBを検索(JPA)し国名(String)を返す
     * 
     */
    public String handleRequest(int countryId) {

        logger.info(String.format("Finding Country with id=%d", countryId)); 

        final EntityManager em = emf.createEntityManager();
        try {
            final Country country = em.find(Country.class, countryId);
            if(Optional.ofNullable(country).isEmpty()){
                throw new RuntimeException(String.format("Country with id=%d was not found.", countryId));
            }
            logger.info(String.format("Found: %s", country.getCountryName()));
            return country.getCountryName();
        }catch(Exception e){
            throw new RuntimeException("An error occured while handling request - " + e.getMessage(), e);
        }finally{
            em.close();
        }

    }


    // ログ初期設定
    static {
        InputStream in = null;
        try{
            in = HelloFunction.class.getClassLoader().getResourceAsStream("logging.properties");
            LogManager.getLogManager().readConfiguration(in);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                in.close();
            }catch(Exception ignore){}
        }
    }

}
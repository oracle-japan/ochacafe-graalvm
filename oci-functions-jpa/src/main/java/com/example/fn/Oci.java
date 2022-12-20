package com.example.fn;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import com.oracle.bmc.auth.BasicAuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.auth.InstancePrincipalsAuthenticationDetailsProvider;
import com.oracle.bmc.auth.ResourcePrincipalAuthenticationDetailsProvider;

public class Oci {

    //private static final Logger logger = Logger.getLogger(Oci.class.getName());
    protected static final BasicAuthenticationDetailsProvider provider = getProvider();

    private static BasicAuthenticationDetailsProvider getProvider(){
        BasicAuthenticationDetailsProvider provider = null;

        // リソース・プリンシパルが使える場合はそれを使う
        final String version = System.getenv("OCI_RESOURCE_PRINCIPAL_VERSION");
        if(Objects.nonNull(version)) {
            provider = ResourcePrincipalAuthenticationDetailsProvider.builder().build();
        }else{
            // ~/.oci/config が存在する場合はユーザ資格証明
            final Path configPath = Paths.get(System.getProperty("user.home"), ".oci/config");
            //logger.fine("configPath: " + configPath);
            if(Files.exists(configPath)){
                try{
                    provider = new ConfigFileAuthenticationDetailsProvider(configPath.toString(), "DEFAULT");
                }catch (IOException e) {
                    throw new RuntimeException("Couldn't create OCI Provider - " + e.getMessage(), e);
                }
            // 最後はインスタンス・プリンシパルで
            }else{
                provider = InstancePrincipalsAuthenticationDetailsProvider.builder().build();
            }
        }
        return provider;
    }

    public BasicAuthenticationDetailsProvider getOciProvider(){
        return provider;
    }


}

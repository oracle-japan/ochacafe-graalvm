package com.example.fn;

import java.io.IOException;
import java.util.Objects;

import com.oracle.bmc.auth.BasicAuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.auth.ResourcePrincipalAuthenticationDetailsProvider;

public class Oci {

    protected static final BasicAuthenticationDetailsProvider provider = getProvider();

    // リソース・プリンシパルが使える場合はそれを使う
    private static BasicAuthenticationDetailsProvider getProvider(){
        String version = System.getenv("OCI_RESOURCE_PRINCIPAL_VERSION");
        BasicAuthenticationDetailsProvider provider = null;
        if(Objects.nonNull(version)) {
            provider = ResourcePrincipalAuthenticationDetailsProvider.builder().build();
        }else{
            try{
                provider = new ConfigFileAuthenticationDetailsProvider("~/.oci/config", "DEFAULT");
            }catch (IOException e) {
                throw new RuntimeException("Couldn't create OCI Provider - " + e.getMessage(), e);
            }
        }
        return provider;
    }

    public BasicAuthenticationDetailsProvider getOciProvider(){
        return provider;
    }


}

package com.example.fn;

import java.util.Base64;

import com.oracle.bmc.keymanagement.KmsVaultClient;
import com.oracle.bmc.keymanagement.requests.ListVaultsRequest;
import com.oracle.bmc.keymanagement.responses.ListVaultsResponse;
import com.oracle.bmc.secrets.SecretsClient;
import com.oracle.bmc.secrets.model.Base64SecretBundleContentDetails;
import com.oracle.bmc.secrets.requests.GetSecretBundleByNameRequest;
import com.oracle.bmc.secrets.requests.GetSecretBundleRequest;
import com.oracle.bmc.secrets.responses.GetSecretBundleByNameResponse;
import com.oracle.bmc.secrets.responses.GetSecretBundleResponse;

public class OciSecrets extends Oci{

    public String getSecretById(String region, String secretOcid){
        try(SecretsClient secretsClient = SecretsClient.builder().build(provider)){
            secretsClient.setRegion(region);
            GetSecretBundleRequest getSecretBundleRequest = GetSecretBundleRequest
                .builder()
                .secretId(secretOcid)
                .stage(GetSecretBundleRequest.Stage.Current)
                .build();
            GetSecretBundleResponse getSecretBundleResponse = secretsClient.getSecretBundle(getSecretBundleRequest);
            Base64SecretBundleContentDetails base64SecretBundleContentDetails =
            (Base64SecretBundleContentDetails) getSecretBundleResponse.
                    getSecretBundle().getSecretBundleContent();
            byte[] secretValueDecoded = Base64.getDecoder().decode(base64SecretBundleContentDetails.getContent());
            return new String(secretValueDecoded);
        }catch(Exception e){
            throw new RuntimeException("Couldn't get content from secret - " + e.getMessage(), e);
        }
    }

    public String getSecretByName(String region, String vaultName, String secretName, String compartmentId){
        String vaultId = null;

        try(KmsVaultClient kmsVaultClient = KmsVaultClient.builder().build(provider)){
            kmsVaultClient.setRegion(region);
            ListVaultsRequest listVaultsRequest = ListVaultsRequest.builder().compartmentId(compartmentId).limit(1024).build();
            ListVaultsResponse listVaultsResponse = kmsVaultClient.listVaults(listVaultsRequest);
            String[] vaultIds = (String[])listVaultsResponse.getItems().stream().filter(v -> v.getDisplayName().equals(vaultName)).map(v -> v.getId()).toArray();
            vaultId = vaultIds[0];
        }catch(Exception e){
            throw new RuntimeException("Couldn't get vault id - " + e.getMessage(), e);
        }
 
        try(SecretsClient secretsClient = SecretsClient.builder().build(provider)){
            secretsClient.setRegion(region);
            GetSecretBundleByNameRequest getSecretBundleByNameRequest = GetSecretBundleByNameRequest
                .builder()
                .vaultId(vaultId)
                .secretName(secretName)
                .stage(GetSecretBundleByNameRequest.Stage.Current)
                .build();
            GetSecretBundleByNameResponse getSecretBundleByNameResponse = secretsClient.getSecretBundleByName(getSecretBundleByNameRequest);
            Base64SecretBundleContentDetails base64SecretBundleContentDetails =
            (Base64SecretBundleContentDetails) getSecretBundleByNameResponse.
                    getSecretBundle().getSecretBundleContent();
            byte[] secretValueDecoded = Base64.getDecoder().decode(base64SecretBundleContentDetails.getContent());
            return new String(secretValueDecoded);
        }catch(Exception e){
            throw new RuntimeException("Couldn't get content from secret - " + e.getMessage(), e);
        }
   }

}

package com.example.fn;

import com.oracle.bmc.secrets.SecretsClient;
import com.oracle.bmc.secrets.model.Base64SecretBundleContentDetails;
import com.oracle.bmc.secrets.requests.GetSecretBundleByNameRequest;
import com.oracle.bmc.secrets.requests.GetSecretBundleRequest;
import com.oracle.bmc.secrets.responses.GetSecretBundleByNameResponse;
import com.oracle.bmc.secrets.responses.GetSecretBundleResponse;

import org.apache.commons.codec.binary.Base64;

public class OciSecrets extends Oci{

    public String getSecretById(String region, String secretOcid){
        try(SecretsClient secretsClient = new SecretsClient(provider)){
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
            byte[] secretValueDecoded = Base64.decodeBase64(base64SecretBundleContentDetails.getContent());
            return new String(secretValueDecoded);
        }catch(Exception e){
            throw new RuntimeException("Couldn't get content from secret - " + e.getMessage(), e);
        }
    }

    public String getSecretByName(String region, String vaultId, String secretName){
        try(SecretsClient secretsClient = new SecretsClient(provider)){
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
            byte[] secretValueDecoded = Base64.decodeBase64(base64SecretBundleContentDetails.getContent());
            return new String(secretValueDecoded);
        }catch(Exception e){
            throw new RuntimeException("Couldn't get content from secret - " + e.getMessage(), e);
        }
    }

}

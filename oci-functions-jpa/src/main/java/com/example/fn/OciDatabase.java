package com.example.fn;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.oracle.bmc.database.DatabaseClient;
import com.oracle.bmc.database.model.GenerateAutonomousDatabaseWalletDetails;
import com.oracle.bmc.database.model.GenerateAutonomousDatabaseWalletDetails.GenerateType;
import com.oracle.bmc.database.requests.GenerateAutonomousDatabaseWalletRequest;
import com.oracle.bmc.database.responses.GenerateAutonomousDatabaseWalletResponse;

class OciDatabase extends Oci{

    private static Logger logger = Logger.getLogger(OciDatabase.class.getName());
    private static SecureRandom secureRandom = new SecureRandom();

    public void downloadWallet(String adbId, String walletDir, String region) throws Exception{
        logger.fine("Wallet dir: " + walletDir);
        if(Objects.isNull(walletDir)){
            logger.fine("Wallet dir not set, skipped to download wallet files.");
            return;
        }else{
            if(Files.exists(Paths.get(walletDir))){
                logger.fine("Wallet dir exists, skipped to download wallet files.");
                return;
            }
        }
        String walletPw = generatePassword(16, false);
        logger.fine("walletPw: " + walletPw);
        Objects.requireNonNull(adbId);
        try(DatabaseClient client = DatabaseClient.builder().build(provider)){
            client.setRegion(region);

            // download wallet
            GenerateAutonomousDatabaseWalletDetails details = GenerateAutonomousDatabaseWalletDetails.builder()
                .generateType(GenerateType.Single)
                .password(walletPw)
                .build();
            GenerateAutonomousDatabaseWalletRequest request = GenerateAutonomousDatabaseWalletRequest.builder()
                .generateAutonomousDatabaseWalletDetails(details)
                .autonomousDatabaseId(adbId)
                .build();
            GenerateAutonomousDatabaseWalletResponse response = client.generateAutonomousDatabaseWallet(request);

            try(ZipInputStream zin = new ZipInputStream(response.getInputStream())){
                Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxr-x---");
                FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
                Files.createDirectories(Paths.get(walletDir), attr);

                ZipEntry zipentry = null;
                while(null != (zipentry = zin.getNextEntry())) {
                    logger.fine("Wallet file: " + zipentry.getName());
                    try(FileOutputStream fout = new FileOutputStream(walletDir + "/" + zipentry.getName());
                            BufferedOutputStream bout = new BufferedOutputStream(fout);
                            ){
                        byte[] data = new byte[1024];
                        int count = 0;
                        while((count = zin.read(data)) != -1){
                            bout.write(data,0,count);
                        }
                    }
                }
            }
        }
        logger.fine("Wallet files download complete.");
    }
    

    private String generatePassword(int length, boolean includeSymbols){
        final String base1 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final String base2 = "!#$%&@";

        final String base = includeSymbols ? base1 + base2 : base1;

        byte bytes[] = new byte[length];
        secureRandom.nextBytes(bytes);

        String result =IntStream.range(0, length)
            .map(i -> bytes[i]).map(n -> (base.length() * (n+128))/256).boxed()
            .map(n -> base.charAt(n))
            .map(c -> new String(new char[]{c})).collect(Collectors.joining(""));
        return result;
    }

}

package com.i1314i.web3j_for_java;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@Slf4j
public class Web3jTests {
    /**
     * 获取eth版本
     * @throws IOException
     */
    @Test
    void getVersion() throws IOException {
        Web3j web3j = Web3j.build(new HttpService("http://localhost:7545"));
        String clientVersion = web3j.web3ClientVersion().send().getWeb3ClientVersion();
        log.info("client version {}",clientVersion);
    }

    /**
     * 创建账户
     */
    @Test
    void createAccount() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        ECKeyPair keyPair= Keys.createEcKeyPair();
        String privateKey=keyPair.getPrivateKey().toString();
        String publicKey=keyPair.getPublicKey().toString();
        String account=Keys.getAddress(keyPair);
        log.info("account {}",account);
    }

}

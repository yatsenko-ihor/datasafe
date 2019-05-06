package de.adorsys.datasafe.business.impl.e2e;

import com.google.common.io.ByteStreams;
import de.adorsys.datasafe.business.api.types.CreateUserPrivateProfile;
import de.adorsys.datasafe.business.api.types.CreateUserPublicProfile;
import de.adorsys.datasafe.business.api.types.UserID;
import de.adorsys.datasafe.business.api.types.UserIDAuth;
import de.adorsys.datasafe.business.api.types.action.ListRequest;
import de.adorsys.datasafe.business.api.types.action.ReadRequest;
import de.adorsys.datasafe.business.api.types.action.WriteRequest;
import de.adorsys.datasafe.business.api.types.keystore.ReadKeyPassword;
import de.adorsys.datasafe.business.api.types.resource.*;
import de.adorsys.datasafe.business.impl.BaseMockitoTest;
import de.adorsys.datasafe.business.impl.service.DefaultDocusafeServices;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseE2ETest extends BaseMockitoTest {

    protected static final String BUCKET_COMPONENT = "bucket";
    protected static final String PRIVATE_COMPONENT = "private";
    protected static final String INBOX_COMPONENT = "inbox";

    protected DefaultDocusafeServices services;

    protected UserIDAuth john;
    protected UserIDAuth jane;
    protected Path tempDir;

    @SneakyThrows
    protected void writeDataToPrivate(UserIDAuth auth, String path, String data) {
        OutputStream stream = services.privateService().write(WriteRequest.forPrivate(auth, path));
        stream.write(data.getBytes());
        stream.close();
    }

    @SneakyThrows
    protected AbsoluteResourceLocation<PrivateResource> getFirstFileInPrivate(UserIDAuth inboxOwner) {
        List<AbsoluteResourceLocation<PrivateResource>> files = services.privateService().list(
                ListRequest.forPrivate(inboxOwner, "./")
        ).collect(Collectors.toList());

        log.info("{} has {} in PRIVATE", inboxOwner.getUserID().getValue(), files);
        return files.get(0);
    }

    @SneakyThrows
    protected String readPrivateUsingPrivateKey(UserIDAuth user, PrivateResource location) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream dataStream = services.privateService().read(ReadRequest.forPrivate(user, location));

        ByteStreams.copy(dataStream, outputStream);
        String data = new String(outputStream.toByteArray());
        log.info("{} has {} in PRIVATE", user.getUserID().getValue(), data);

        return data;
    }

    @SneakyThrows
    protected String readInboxUsingPrivateKey(UserIDAuth user, PrivateResource location) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream dataStream = services.inboxService().read(ReadRequest.forPrivate(user, location));

        ByteStreams.copy(dataStream, outputStream);
        String data = new String(outputStream.toByteArray());
        log.info("{} has {} in INBOX", user.getUserID().getValue(), data);

        return data;
    }

    @SneakyThrows
    protected AbsoluteResourceLocation<PrivateResource>  getFirstFileInInbox(UserIDAuth inboxOwner) {
        List<AbsoluteResourceLocation<PrivateResource>> files = services.inboxService().list(
               ListRequest.forPrivate(inboxOwner, "./")
        ).collect(Collectors.toList());

        log.info("{} has {} in INBOX", inboxOwner.getUserID().getValue(), files);
        return files.get(0);
    }

    protected void registerJohnAndJane() {
        john = registerUser("john");
        jane = registerUser("jane");
    }

    @SneakyThrows
    protected void sendToInbox(UserID from, UserID to, String filename, String data) {
        OutputStream stream = services.inboxService().write(WriteRequest.forPublic(to, "./" + filename));
        stream.write(data.getBytes());
        stream.close();
    }

    @SneakyThrows
    protected UserIDAuth registerUser(String userName) {
        UserIDAuth auth = new UserIDAuth();
        auth.setUserID(new UserID(userName));
        auth.setReadKeyPassword(new ReadKeyPassword("secure-password " + userName));

        String userPath = "./" + BUCKET_COMPONENT + "/" + userName + "/";

        services.userProfile().registerPublic(CreateUserPublicProfile.builder()
                .id(auth.getUserID())
                .inbox(access(new URI(userPath).resolve("./" + INBOX_COMPONENT + "/")))
                .publicKeys(access(new URI(userPath).resolve("./keystore")))
                .build()
        );

        services.userProfile().registerPrivate(CreateUserPrivateProfile.builder()
                .id(auth)
                .privateStorage(accessPrivate(new URI(userPath).resolve("./" + PRIVATE_COMPONENT + "/")))
                .keystore(accessPrivate(new URI(userPath).resolve("./keystore")))
                .inboxWithWriteAccess(accessPrivate(new URI(userPath).resolve("./" + INBOX_COMPONENT + "/")))
                .build()
        );

        return auth;
    }

    private AbsoluteResourceLocation<PublicResource> access(URI path) {
        return new AbsoluteResourceLocation<>(new DefaultPublicResource(tempDir.toUri().resolve(path)));
    }

    private AbsoluteResourceLocation<PrivateResource> accessPrivate(URI path) {
        return new AbsoluteResourceLocation<>(new DefaultPrivateResource(
                tempDir.toUri().resolve(path), URI.create(""), URI.create(""))
        );
    }
}

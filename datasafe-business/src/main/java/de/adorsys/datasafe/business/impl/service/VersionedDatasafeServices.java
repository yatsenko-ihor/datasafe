package de.adorsys.datasafe.business.impl.service;

import dagger.BindsInstance;
import dagger.Component;
import de.adorsys.datasafe.business.api.config.DFSConfig;
import de.adorsys.datasafe.business.api.inbox.InboxServiceImpl;
import de.adorsys.datasafe.business.api.storage.actions.*;
import de.adorsys.datasafe.business.impl.cmsencryption.DefaultCMSEncryptionModule;
import de.adorsys.datasafe.business.impl.directory.DefaultCredentialsModule;
import de.adorsys.datasafe.business.impl.directory.DefaultProfileModule;
import de.adorsys.datasafe.business.impl.document.DefaultDocumentModule;
import de.adorsys.datasafe.business.impl.inbox.actions.DefaultInboxActionsModule;
import de.adorsys.datasafe.business.impl.keystore.DefaultKeyStoreModule;
import de.adorsys.datasafe.business.impl.pathencryption.DefaultPathEncryptionModule;
import de.adorsys.datasafe.business.impl.privatespace.PrivateSpaceServiceImpl;
import de.adorsys.datasafe.business.impl.privatestore.actions.DefaultPrivateActionsModule;
import de.adorsys.datasafe.business.impl.privatestore.actions.DefaultVersionedPrivateActionsModule;
import de.adorsys.datasafe.business.impl.profile.operations.DFSBasedProfileStorageImpl;
import de.adorsys.datasafe.business.impl.version.latest.DefaultVersionInfoServiceImpl;
import de.adorsys.datasafe.business.impl.version.latest.LatestPrivateSpaceImpl;
import de.adorsys.datasafe.business.impl.version.types.LatestDFSVersion;

import javax.inject.Singleton;

/**
 * This is Datasafe services that always work with latest file version.
 */
@Singleton
@Component(modules = {
        DefaultCredentialsModule.class,
        DefaultKeyStoreModule.class,
        DefaultDocumentModule.class,
        DefaultCMSEncryptionModule.class,
        DefaultPathEncryptionModule.class,
        DefaultInboxActionsModule.class,
        DefaultPrivateActionsModule.class,
        DefaultVersionedPrivateActionsModule.class,
        DefaultProfileModule.class
})
public interface VersionedDatasafeServices {

    /**
     * @return Provides version information for a given resource
     */
    DefaultVersionInfoServiceImpl versionInfo();

    /**
     * @return Filtered view of user's private space, that shows only latest files
     */
    LatestPrivateSpaceImpl<LatestDFSVersion> latestPrivate();

    /**
     * @return Raw view of private user space (all file versions are visible)
     */
    PrivateSpaceServiceImpl privateService();

    /**
     * @return Inbox service that provides capability to share data between users
     */
    InboxServiceImpl inboxService();

    /**
     * @return User-profile service that provides information necessary for locating his data.
     */
    DFSBasedProfileStorageImpl userProfile();

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder config(DFSConfig config);

        @BindsInstance
        Builder storageList(StorageListService listService);

        @BindsInstance
        Builder storageRead(StorageReadService readService);

        @BindsInstance
        Builder storageWrite(StorageWriteService writeService);

        @BindsInstance
        Builder storageRemove(StorageRemoveService removeService);

        @BindsInstance
        Builder storageCheck(StorageCheckService checkService);

        VersionedDatasafeServices build();
    }
}

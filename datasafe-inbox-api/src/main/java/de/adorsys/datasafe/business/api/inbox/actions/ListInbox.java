package de.adorsys.datasafe.business.api.inbox.actions;

import de.adorsys.datasafe.business.api.types.UserIDAuth;
import de.adorsys.datasafe.business.api.types.action.ListRequest;
import de.adorsys.datasafe.business.api.types.resource.PrivateResource;

import java.util.stream.Stream;

public interface ListInbox {

    Stream<PrivateResource> list(ListRequest<UserIDAuth> forUser);
}

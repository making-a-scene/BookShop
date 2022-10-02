package shoppingmall.bookshop.authentication.OAuth2;

public interface OAuth2UserInfo {
    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
}


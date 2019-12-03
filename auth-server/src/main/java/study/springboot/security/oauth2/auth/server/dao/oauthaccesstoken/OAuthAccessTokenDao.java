package study.springboot.security.oauth2.auth.server.dao.oauthaccesstoken;

public interface OAuthAccessTokenDao {

    int insert(OAuthAccessTokenEO entity);

    int update(OAuthAccessTokenEO entity);
}

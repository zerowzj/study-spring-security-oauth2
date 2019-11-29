package study.springboot.security.jwt.auth.access;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

@Component("metadataSource")
public class FunctionMapFactoryBean implements FactoryBean<LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>> {

    @Override
    public LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> getObject() throws Exception {
        //初始化
        LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> metadataSource = new LinkedHashMap<>();
        //获取受保护功能列表
        String pfPath = "/tests";
        AntPathRequestMatcher matcher = new AntPathRequestMatcher(pfPath + "*");
        List<ConfigAttribute> configAttrLt = Lists.newArrayList(new SecurityConfig(pfPath));
        metadataSource.put(matcher, configAttrLt);
        return metadataSource;
    }

    @Override
    public Class<?> getObjectType() {
        return LinkedHashMap.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}

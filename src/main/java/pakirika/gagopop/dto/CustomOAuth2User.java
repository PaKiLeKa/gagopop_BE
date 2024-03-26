package pakirika.gagopop.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final UserDTO userDTO;

    public CustomOAuth2User(UserDTO userDTO) {
        this.userDTO=userDTO;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    } //획일화하기 힘들어 사용하지 않을 것

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add( new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userDTO.getRole();
            }
        } );

        return collection;
    }

    @Override
    public String getName() {
        return userDTO.getName();
    }

    public String getUsername(){
        return userDTO.getUsername(); //id값으로 사용할 것!
    }

}

package kots.service;

import kots.controller.dto.RoleDto;
import kots.controller.dto.UserDto;
import kots.exception.ObjectNotFoundException;
import kots.model.Role;
import kots.model.User;
import kots.repository.RoleRepository;
import kots.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;

import static kots.service.mapper.RoleMapper.toRoleDtos;
import static kots.service.mapper.UserMapper.toUserDto;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserDto saveUser(User user) {
        return toUserDto(userRepository.save(user));
    }

    public RoleDto saveRole(Role role) {
        return toRoleDtos(roleRepository.save(role));
    }

    @Transactional
    public UserDto addRoleToUser(String username, String roleName) {
        User user = getUser(username);
        Role role = getRole(roleName);
        user.getRoles().add(role);
        return toUserDto(user);
    }

    public UserDto getSingleUser(String username) {
        return toUserDto(getUser(username));
    }

    public User getUser(String name) {
        return userRepository.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));
    }

    private Role getRole(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException("Role not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByName(username).orElseThrow(() -> new UsernameNotFoundException("User not exist in database"));
        return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), getAuthorities(user.getRoles()));
    }

    private Collection<SimpleGrantedAuthority> getAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();
    }
}

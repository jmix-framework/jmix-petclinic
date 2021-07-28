package io.jmix.petclinic.app;

import io.jmix.petclinic.entity.User;
import io.jmix.petclinic.security.DatabaseUserRepository;
import io.jmix.petclinic.security.NurseRole;
import io.jmix.security.role.assignment.RoleAssignment;
import io.jmix.security.role.assignment.RoleAssignmentRepository;
import io.jmix.security.role.assignment.RoleAssignmentRoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("petclinic_EmployeeRepository")
public class EmployeeRepository {

    @Autowired
    private RoleAssignmentRepository roleAssignmentRepository;
    @Autowired
    private DatabaseUserRepository databaseUserRepository;

    public List<User> findAllNurses() {
        return roleAssignmentRepository.getAllAssignments()
                .stream()
                .filter(roleAssignment -> roleAssignment.getRoleType().equals(RoleAssignmentRoleType.RESOURCE))
                .filter(roleAssignment -> roleAssignment.getRoleCode().equals(NurseRole.CODE))
                .map(RoleAssignment::getUsername)
                .distinct()
                .map(username -> databaseUserRepository.loadUserByUsername(username))
                .collect(Collectors.toList());
    }
}
package com.servify.admin.service;

import com.servify.admin.dto.AdminDashboardStats;
import com.servify.admin.dto.AdminRequest;
import com.servify.admin.dto.AdminResponse;
import com.servify.admin.dto.ProviderApplicationResponse;
import com.servify.admin.mapper.AdminMapper;
import com.servify.admin.model.AdminEntity;
import com.servify.admin.repository.AdminRepository;
import com.servify.client.dto.ClientResponse;
import com.servify.client.mapper.ClientMapper;
import com.servify.client.repository.ClientRepository;
import com.servify.provider.model.ProviderEntity;
import com.servify.provider.model.ProviderStatus;
import com.servify.provider.repository.ProviderRepository;
import com.servify.provider.repository.ServiceCategoryRepository;
import com.servify.provider.service.SearchOptionsService;
import com.servify.shared.exception.ResourceNotFoundException;
import com.servify.user.UserNotFoundException;
import com.servify.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @Mock
    private AdminRepository adminRepository;
    @Mock
    private ProviderRepository providerRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AdminMapper adminMapper;
    @Mock
    private ClientMapper clientMapper;
    @Mock
    private SearchOptionsService searchOptionsService;
    @Mock
    private ServiceCategoryRepository serviceCategoryRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    private AdminRequest adminRequest;
    private AdminEntity adminEntity;
    private AdminResponse adminResponse;

    @BeforeEach
    void setUp() {
        adminRequest = new AdminRequest();
        adminRequest.setEmail("admin@example.com");
        adminRequest.setPassword("password");
        adminRequest.setName("Admin");
        adminRequest.setGovernorate("tunis");
        adminRequest.setPhone("1234");

        adminEntity = new AdminEntity();
        adminEntity.setEmail("admin@example.com");

        adminResponse = new AdminResponse();
        adminResponse.setEmail("admin@example.com");
    }

    @Test
    void create_shouldPersistAdminWhenPasswordPresent() {
        when(userRepository.existsByEmail(adminRequest.getEmail())).thenReturn(false);
        when(adminMapper.toEntity(adminRequest)).thenReturn(adminEntity);
        when(adminRepository.save(adminEntity)).thenReturn(adminEntity);
        when(adminMapper.toResponse(adminEntity)).thenReturn(adminResponse);

        AdminResponse result = adminService.create(adminRequest);

        assertNotNull(result);
        assertEquals(adminResponse.getEmail(), result.getEmail());
        verify(adminRepository).save(adminEntity);
        verify(adminMapper).toResponse(adminEntity);
    }

    @Test
    void create_shouldThrowWhenPasswordMissing() {
        adminRequest.setPassword(" ");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> adminService.create(adminRequest));

        assertThat(exception).hasMessage("Password is required for admin creation");
        verify(adminRepository, never()).save(any());
    }

    @Test
    void findAll_shouldReturnMappedResponses() {
        AdminEntity anotherAdmin = new AdminEntity();
        AdminResponse anotherResponse = new AdminResponse();
        when(adminRepository.findAll()).thenReturn(List.of(adminEntity, anotherAdmin));
        when(adminMapper.toResponse(adminEntity)).thenReturn(adminResponse);
        when(adminMapper.toResponse(anotherAdmin)).thenReturn(anotherResponse);

        List<AdminResponse> responses = adminService.findAll();

        assertThat(responses).containsExactly(adminResponse, anotherResponse);
        verify(adminRepository).findAll();
    }

    @Test
    void findById_shouldThrowWhenNotFound() {
        when(adminRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> adminService.findById(1L));
    }

    @Test
    void update_shouldRefreshEntityAndReturnResponse() {
        AdminRequest updatedRequest = new AdminRequest();
        updatedRequest.setEmail("new@example.com");
        updatedRequest.setName("New Name");
        updatedRequest.setPhone("555");
        updatedRequest.setGovernorate("tunis");

        when(adminRepository.findById(5L)).thenReturn(Optional.of(adminEntity));
        when(userRepository.existsByEmail(updatedRequest.getEmail())).thenReturn(false);
        when(adminMapper.toResponse(adminEntity)).thenReturn(adminResponse);

        AdminResponse result = adminService.update(5L, updatedRequest);

        assertSame(adminResponse, result);
        verify(adminMapper).updateEntity(adminEntity, updatedRequest);
    }

    @Test
    void delete_shouldRemoveWhenExists() {
        when(adminRepository.existsById(10L)).thenReturn(true);

        adminService.delete(10L);

        verify(adminRepository).deleteById(10L);
    }

    @Test
    void delete_shouldThrowWhenAdminMissing() {
        when(adminRepository.existsById(11L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> adminService.delete(11L));
        verify(adminRepository, never()).deleteById(any());
    }

    @Test
    void getDashboardStats_shouldAggregateCounts() {
        when(providerRepository.count()).thenReturn(3L);
        when(clientRepository.count()).thenReturn(4L);
        when(adminRepository.count()).thenReturn(2L);
        when(userRepository.count()).thenReturn(9L);
        when(serviceCategoryRepository.count()).thenReturn(6L);
        when(providerRepository.countByStatus(ProviderStatus.PENDING)).thenReturn(1L);
        when(providerRepository.countByStatus(ProviderStatus.ACCEPTED)).thenReturn(2L);
        when(providerRepository.countByStatus(ProviderStatus.REJECTED)).thenReturn(0L);

        AdminDashboardStats stats = adminService.getDashboardStats();

        assertThat(stats.getProviders()).isEqualTo(3L);
        assertThat(stats.getClients()).isEqualTo(4L);
        assertThat(stats.getAdmins()).isEqualTo(2L);
        assertThat(stats.getUsers()).isEqualTo(9L);
        assertThat(stats.getServices()).isEqualTo(6L);
        assertThat(stats.getPendingProviders()).isEqualTo(1L);
        assertThat(stats.getAcceptedProviders()).isEqualTo(2L);
        assertThat(stats.getRejectedProviders()).isZero();
    }

    @Test
    void findProviderApplications_shouldUseStatusWhenProvided() {
        ProviderEntity provider = new ProviderEntity();
        ProviderApplicationResponse response = new ProviderApplicationResponse();
        when(providerRepository.findAllByStatus(ProviderStatus.PENDING)).thenReturn(List.of(provider));
        when(adminMapper.toProviderApplication(provider)).thenReturn(response);

        List<ProviderApplicationResponse> responses = adminService.findProviderApplications(ProviderStatus.PENDING);

        assertThat(responses).containsExactly(response);
        verify(providerRepository, never()).findAll();
    }

    @Test
    void findProviderApplications_shouldFallbackToAllWhenStatusNull() {
        ProviderEntity provider = new ProviderEntity();
        when(providerRepository.findAll()).thenReturn(List.of(provider));
        ProviderApplicationResponse response = new ProviderApplicationResponse();
        when(adminMapper.toProviderApplication(provider)).thenReturn(response);

        List<ProviderApplicationResponse> responses = adminService.findProviderApplications(null);

        assertThat(responses).containsExactly(response);
        verify(providerRepository).findAll();
    }

    @Test
    void updateProviderStatus_shouldRegisterSearchOptionsOnAccept() {
        ProviderEntity provider = new ProviderEntity();
        provider.setStatus(ProviderStatus.PENDING);
        provider.setServiceCategory("plumbing");
        provider.setGovernorate("tunis");
        ProviderApplicationResponse response = new ProviderApplicationResponse();

        when(providerRepository.findById(99L)).thenReturn(Optional.of(provider));
        when(adminMapper.toProviderApplication(provider)).thenReturn(response);

        ProviderApplicationResponse result = adminService.updateProviderStatus(99L, ProviderStatus.ACCEPTED);

        assertSame(response, result);
        assertThat(provider.getStatus()).isEqualTo(ProviderStatus.ACCEPTED);
        verify(searchOptionsService).registerServiceAndGovernorate("plumbing", "tunis");
    }

    @Test
    void updateProviderStatus_shouldThrowWhenProviderMissing() {
        when(providerRepository.findById(101L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> adminService.updateProviderStatus(101L, ProviderStatus.ACCEPTED));
    }

    @Test
    void deleteProvider_shouldRemoveExistingProvider() {
        ProviderEntity provider = new ProviderEntity();
        when(providerRepository.findById(55L)).thenReturn(Optional.of(provider));

        adminService.deleteProvider(55L);

        verify(providerRepository).delete(provider);
    }

    @Test
    void deleteProvider_shouldThrowWhenMissing() {
        when(providerRepository.findById(56L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adminService.deleteProvider(56L));
    }

    @Test
    void findAllClients_shouldMapResponses() {
        ClientResponse clientResponse = new ClientResponse();
        when(clientRepository.findAll()).thenReturn(List.of(new com.servify.client.model.ClientEntity()));
        when(clientMapper.toResponse(any())).thenReturn(clientResponse);

        List<ClientResponse> responses = adminService.findAllClients();

        assertThat(responses).containsExactly(clientResponse);
    }

    @Test
    void deleteClient_shouldThrowWhenMissing() {
        when(clientRepository.existsById(70L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> adminService.deleteClient(70L));
    }

    @Test
    void deleteClient_shouldRemoveWhenExists() {
        when(clientRepository.existsById(71L)).thenReturn(true);

        adminService.deleteClient(71L);

        verify(clientRepository).deleteById(71L);
    }
}
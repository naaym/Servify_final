package com.servify.admin.mapper;

import com.servify.admin.dto.AdminRequest;
import com.servify.admin.dto.AdminResponse;
import com.servify.admin.dto.ProviderApplicationResponse;
import com.servify.admin.model.AdminEntity;
import com.servify.provider.model.ProviderEntity;

public interface AdminMapper {
    AdminEntity toEntity(AdminRequest request);

    void updateEntity(AdminEntity entity, AdminRequest request);

    AdminResponse toResponse(AdminEntity entity);

    ProviderApplicationResponse toProviderApplication(ProviderEntity providerEntity);
}

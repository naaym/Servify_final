package com.servify.admin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminDashboardStats {
    private long users;
    private long admins;
    private long providers;
    private long clients;
    private long services;
    private long pendingProviders;
    private long acceptedProviders;
    private long rejectedProviders;
}

# Code Review Notes

## Summary
- Fixed the login flow overwriting the stored access token by persisting the user id under a dedicated key via `TokenService`.
- Centralized storage responsibilities inside `TokenService` for better consistency and future auditing.
- Captured follow-up recommendations to harden auth flows and HTTP helpers.

## Findings & Recommendations
1. **Token storage overwrite (fixed)** – `AuthService.login` stored both the JWT and user id under the same `ACCESS_TOKEN` key, so the id overwrote the token before any downstream use. The login flow now saves the access token and user id through `TokenService`, ensuring the token remains intact and storage logic is centralized. 
2. **Token lifecycle management** – `TokenService` now tracks both the access token and user id, but the `clearTokens` method should be invoked on logout to wipe both values; ensure logout handlers call it consistently.
3. **Interceptor hygiene** – `authInterceptor` logs every intercepted request and does not handle token refresh or 401 fallback. Consider removing console logging in production builds and adding refresh/retry logic so users are not silently signed out when tokens expire.
4. **HTTP helper consistency** – The custom `Http` service mixes base URLs with and without trailing slashes (e.g., `BASE` already ends with `/`, while some helpers append another `/`). This can lead to double slashes or missing separators depending on the endpoint string. Consider normalizing path joining or using `HttpParams` options instead of manual string concatenation.
5. **Login component UX** – `Login` subscribes directly to the auth call but never disables the form during submission or guards against multiple clicks. Adding a pending state and explicit error surface (instead of relying solely on toast service) will prevent duplicate requests and improve accessibility.

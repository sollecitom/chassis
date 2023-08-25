# Actor model

- Actor
    - Unknown (for endpoints available before login)
    - Known
        - User Account
            - External User
            - Internal User (sales rep, customer support operator, etc.)
        - Service Account
            - External system
            - Internal system (internal microservice, etc.)

- Authentication Mechanism
    - Unknown actor: none
    - Known actor:
        - User Account:
            - Session-based
            - Token-based (mmm JWTs are tokens, but belong to Session-based. A better name for API keys?)
        - Service Account:
            - Session-based (consent-based? but that's acting on behalf, mmm)
            - Token-based (mmm JWTs are tokens, but belong to Session-based. A better name for API keys?)

- Acting on behalf
    - Use cases:
        - Internal user on behalf of external user
        - External system on behalf on external user
        - Internal system on behalf of external or internal user
    - How it works:
        - The roles belong to the actual actor
        - The side effects belong to the actor that's been acted on behalf of
        - Logged appropriately for audit

- Impersonation
    - Use cases:
        - Internal user on behalf of external or internal user
    - How it works:
        - The roles belong to the impersonated user (can be a subset)
        - The side effects also belong to the impersonated user
    - Logged appropriately for audit

Notes:

- Sounds like authentication mechanism should be a property of Actor, rather than a different field in the Invocation Context.
- Can an actor or user exist across tenants? No. -> sounds like Tenant should be a property of Actor, rather than a different field in Invocation Context.
- Does actor need a separate User or Account as a property? It would help in a number of ways:
    - By preventing recursive impersonation or acting on behalf which makes little sense.
    - By allowing to send/receive an Account when doing certain things
    - Should it be known Account, rather than known Actor?
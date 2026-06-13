# Food Delivery System

## Overview

A full-featured food delivery platform built with Spring Boot, following a feature-based architecture. The system allows customers to browse restaurants, place orders, and track deliveries in real time. Restaurant owners can manage their menus, monitor incoming orders, and view business reports. A system admin oversees the platform — managing users, restaurants, and generating operational reports.

The platform covers the full delivery lifecycle: user registration and authentication, restaurant and menu management, cart and order processing, customer account management, payment integration with third-party providers, and a reporting dashboard for both admins and restaurant owners.

---

## Actors

| Actor | Description |
|---|---|
| **Customer** | Registers, browses restaurants, manages a cart, places and tracks orders, and manages their account and payment preferences. |
| **Restaurant Owner** | Registers and manages their restaurant, creates and maintains menus, processes incoming orders, and views restaurant-level reports. |
| **System Admin** | Manages the entire platform — enables/disables accounts and restaurants, monitors system-wide statistics, and generates platform-level reports. |

---

## Features & API Endpoints

### 1. User Registration & Authentication

Handles all identity and access concerns: sign-up flows for customers and restaurants, authentication, OTP verification, social login, and role-based permissions.

| Method | Endpoint | Description |
|---|---|---|
| POST | `/auth/signup` | Sign up |
| POST | `/auth/login` | Login |
| POST | `/auth/logout` | Logout |
| POST | `/auth/forget-password` | Forget password |
| POST | `/auth/verify-otp` | Email / SMS OTP verification |
| GET / PUT | `/users/{id}/profile` | View / update user profile |
| POST | `/auth/social-login` | Social media authentication |
| PATCH | `/users/{id}/status` | Enable or disable account |

---

### 2. Restaurant & Menu Management

Allows restaurant owners to register and manage their restaurant, maintain menus, and lets customers search and discover restaurants.

| Method | Endpoint | Description |
|---|---|---|
| POST | `/restaurants` | Register restaurant |
| PUT | `/restaurants/{id}` | Update restaurant |
| PATCH | `/restaurants/{id}/status` | Enable / disable restaurant |
| GET | `/restaurants` | View all restaurants |
| GET | `/restaurants/top-rated` | Top rating restaurants |
| GET | `/restaurants/recommendations` | Restaurant recommendations |
| GET | `/restaurants/search` | Search restaurants |
| POST | `/restaurants/{id}/menus` | Create a new menu |
| PUT | `/restaurants/{id}/menus/{menuId}` | Update menu |
| DELETE | `/restaurants/{id}/menus/{menuId}` | Delete menu |
| PATCH | `/restaurants/{id}/menus/{menuId}/status` | Enable / disable menu |
| GET | `/restaurants/{id}/menus/history` | View history list of menus |
| GET | `/restaurants/{id}/menus/search` | Search menu items |

---

### 3. Cart Management

Manages a customer's shopping cart — adding and modifying items, viewing cart contents, and proceeding to checkout.

| Method | Endpoint | Description |
|---|---|---|
| POST | `/cart/items` | Add to cart |
| PUT | `/cart/items/{itemId}` | Modify cart item |
| GET | `/cart` | View cart |
| DELETE | `/cart` | Clear cart |
| DELETE | `/cart/items/{itemId}` | Remove item from cart |
| POST | `/cart/checkout` | Checkout |
| PATCH | `/cart/items/{itemId}/quantity` | Update item quantity |

#### 3.1 Add to cart flowchart

```mermaid
flowchart TB
    n1([Start]) --> n2["Get user"]
    n2 --> n12{"Is user authenticated?"}

    n12 -- No --> n13([Return to login page])
    n12 -- Yes --> n3{"Does user have a cart?"}

    n3 -- No --> n10["Create new cart"]
    n3 -- Yes --> n11["Get user cart"]

    n10 --> n5
    n11 --> n5

    n5{"Same restaurant (if cart not empty)?"}
    n5 -- Not valid --> n6([Return error: clear cart first])
    n5 -- Valid --> n7{"Is item available?"}

    n7 -- Not valid --> n8([Return item unavailable])
    n7 -- Valid --> n9{"Item already in cart?"}

    n9 -- No --> n14["Add item to cart"]
    n9 -- Yes --> n15["Update item quantity"]

    n14 --> n16([Return success message])
    n15 --> n16
```

### 3.2 Increase item quantity flowchart

```mermaid
flowchart TB
    n1([Start]) --> n2["Get user"]
    n2 --> n3{"Is user authenticated?"}

    n3 -- No --> n4([Return to login page])
    n3 -- Yes --> n5["Get user cart"]

    n5 --> n6{"Does cart contain the target item?"}
    n6 -- No --> n7([Return error message])
    n6 -- Yes --> n8{"Is item with target quantity available?"}

    n8 -- No --> n9([Return unavailable message])
    n8 -- Yes --> n10["Increase item quantity by one"]

    n10 --> n11([Return])
```

#### 3.3 Decrease item quantity flowchart

```mermaid
flowchart TB
    n1([Start]) --> n2["Get user"]
    n2 --> n3{"Is user authenticated?"}

    n3 -- No --> n4([Return to login page])
    n3 -- Yes --> n5["Get user cart"]

    n5 --> n6{"Does cart contain the target item?"}
    n6 -- No --> n7([Return error message])
    n6 -- Yes --> n8{"Is item with target quantity available?"}

    n8 -- No --> n9([Return unavailable message])
    n8 -- Yes --> n10["Decrease item quantity by one"]

    n10 --> n12{"Is cart item quantity <= 0?"}
    n12 -- No --> n13([Return])
    n12 -- Yes --> n14["Remove cart item"]

    n14 --> n13
```

#### 3.4 View cart flowchart

```mermaid
flowchart TB
    n1([Start]) --> n2["Get user"]
    n2 --> n3{"Is user authenticated?"}

    n3 -- No --> n4([Return to login page])
    n3 -- Yes --> n5{"Does user have a cart?"}

    n5 -- No --> n6([Return empty cart])
    n5 -- Yes --> n7["Get cart with all items"]

    n7 --> n8{"Is cart empty?"}
    n8 -- Yes --> n6
    n8 -- No --> n9([Return cart items with totals])
```

#### 3.5 Clear all items flowchart

```mermaid
flowchart TB
    n1([Start]) --> n2["Fetch customer with cart by user ID"]
    n2 --> n3{"Is cart null?"}

    n3 -- Yes --> n4([Return error: cart not found])
    n3 -- No --> n5["Clear the cart"]

    n5 --> n6([Return success message])
```

#### 3.6 Remove item flowchart

```mermaid
flowchart TB
    n1([Start]) --> n2["Get user"]
    n2 --> n3{"Is user authenticated?"}

    n3 -- No --> n4([Return to login page])
    n3 -- Yes --> n5["Get user cart"]

    n5 --> n6{"Does cart contain the target item?"}
    n6 -- No --> n7([Return error: item not found in cart])
    n6 -- Yes --> n8["Remove item from cart"]

    n8 --> n9([Return success message])

```

---

### 4. Order Management

Handles the full order lifecycle from placement to completion, including cancellations, status updates, and order history for restaurants.

| Method | Endpoint | Description |
|---|---|---|
| POST | `/orders` | Place order |
| DELETE | `/orders/{id}` | Cancel order (by customer or restaurant) |
| PATCH | `/orders/{id}/status` | Update order status |
| GET | `/restaurants/{id}/orders` | Restaurant order history |
| GET | `/orders/{id}/summary` | Order summary |
| GET | `/orders/{id}` | Order details |

> **Notifications:** Order confirmation is sent via email / SMS upon placement. Customers are notified on every order status change via push notification and SMS/email.

#### <div align="center"> Diagrams</div>
##### Order State Diagram
```mermaid

stateDiagram-v2
    direction TB
    
    [*] --> PENDING : Order Created

    PENDING --> IN_PROGRESS : Advance Status
    IN_PROGRESS --> ON_THE_WAY : Advance Status
    ON_THE_WAY --> DELIVERED : Advance Status

    PENDING --> CANCELLED : Cancel Order (Restaurant)
    IN_PROGRESS --> CANCELLED : Cancel Order (Restaurant)
    ON_THE_WAY --> CANCELLED : Cancel Order (Restaurant)
    DELIVERED --> CANCELLED : Could be cancelled as <br>a refund (Restaurant)
    note right of DELIVERED
        Terminal State:
        Throws BadRequestException
        if advanced.
    end note
    
    note right of CANCELLED
        Terminal State:
        Throws BadRequestException
        if advanced.
    end note

    DELIVERED --> [*]
    CANCELLED --> [*]
```
##### 4.2.1 Update order status flowchart
```mermaid
flowchart TB
    db([Database])

    start(["Start"]) --> auth{"Has Admin Role"}
    auth -- YES --> update{"Fetch required order with restaurant <br>branch admin validation"}
    update --> db
    db -- result --> update
    auth -- "NO - 403" ----> en
    update -- "NOT - FOUND " --> en(["End"])
    update -- YES -->  tracking["New order tracking"]
    tracking --> mail["Send email Asynchronously"]
    mail -- "Transaction committed - 204" --> en


```
##### 4.2.2 Place Order (Chain of Responsibility) Sequence Diagram
```mermaid
sequenceDiagram
    autonumber
    actor Client
    participant Service as OrderServiceImpl
    participant Context as OrderProcessingContext
    participant H1 as CartValidation
    participant H2 as OpenTimeValidation
    participant H3 as MenuItemValidation
    participant H4 as FinalizeOrder
    participant H5 as PaymentProcess
    participant DB as PostgreSQL
    participant Pub as EventPublisher
    participant Email as EmailService

    Client->>Service: placeOrder(request, customerId)
    activate Service
    
    Note over Service, Context: Phase 1: Context Initialization
    Service->>Context: buildContext(lambdas...)
    activate Context
    Context-->>Service: context
    deactivate Context

    Note over Service, H5: Phase 2: Explicit Chain Execution
    Service->>H1: handle(context)
    activate H1

    %% Handler 1
    H1->>Context: getCart()
    Context->>DB: getCartByIdAndCustomerIdWithLock()
    DB-->>Context: Cart (Locked)
    H1->>H1: validate restaurant match
    H1->>H1: handleNext(context)
    H1->>H2: handle(context)
    activate H2

    %% Handler 2

    H2->>Context: getRestaurantBranch() 
    H2->>H2: validate branch.isOpen()
    H2->>H2: handleNext(context)
    H2->>H3: handle(context)
    activate H3

    %% Handler 3
    H3->>Context: getMenuItems()
    Context->>Context: getCartItems()
    Context->>DB: getCartItemsWithDetails() [JOIN FETCH]
    DB-->>Context: Hydrated Items
    H3->>H3: validateCartItemsAvailability()
    H3->>H3: handleNext(context)
    H3->>H4: handle(context)
    activate H4

    %% Handler 4
    H4->>Context: getCustomer() & getCoupon()
    Context->>DB: Fetch remaining dependencies
    H4->>H4: createAndPersistOrder() (Build Graph in Memory)
    H4->>DB: orderRepository.save(order)
    DB-->>H4: savedOrder
    H4->>Context: setResponseDto(mapped DTO)
    H4->>H4: handleNext(context)
    H4->>H5: handle(context)
    activate H5
    
    %% Handler 5
    H5->>H5: paymentService.processPayment()
    H5->>H5: handleNext(context) (next == null)
    
    %% The Stack Unwinds
    Note over H1, H5: The Call Stack Returns (Unwinding)
    H5-->>H4: orderResponseDto
    deactivate H5
    H4-->>H3: orderResponseDto
    deactivate H4
    H3-->>H2: orderResponseDto
    deactivate H3
    H2-->>H1: orderResponseDto
    deactivate H2
    H1-->>Service: orderResponseDto
    deactivate H1

    Note over Service, Pub: Phase 3: Event Publication
    Service->>Context: getCustomerEmail()
    Service->>Pub: publishEvent(new EmailEventRecord(...))
    
    Note over Service, DB: @Transactional Boundary Ends -> Commit
    Service-->>Client: orderResponseDto
    deactivate Service
    
    Note over DB, Email: Phase 4: Async Post-Commit Execution
    DB-->>Pub: Transaction SUCCESS Signal
    activate Pub
    Pub->>Email: sendEmailAsync(EmailEventRecord)
    deactivate Pub
    activate Email
    Email-->>Email: Attempt SMTP send on background thread
    deactivate Email

```
---

### 5. Customer Management

Covers customer self-service features: order history, address book, payment preferences, ratings, order tracking, and account management.

| Method | Endpoint | Description |
|---|---|---|
| GET | `/customers/{id}/orders` | Customer order history |
| GET / PUT | `/customers/{id}/payment-settings` | View / update preferred payment settings |
| GET / POST / PUT / DELETE | `/customers/{id}/addresses` | Address management |
| PATCH | `/customers/{id}/deactivate` | Deactivate account |
| POST | `/orders/{id}/rating` | Submit rating & comments |
| GET | `/orders/{id}/tracking` | Track order status |

> **Additional:** In-app customer support chat is available via chat integration.

---

### 6. Payment Integration

Handles payment processing through third-party providers, transaction history, receipt generation, and payment auditing and validation.

| Method | Endpoint | Description |
|---|---|---|
| POST | `/payments` | Initiate payment (3rd-party integration) |
| GET | `/payments/transactions` | View payment transactions |
| GET | `/payments/transactions/{id}/receipt` | Generate transaction receipt |

> **Additional:** Payment auditing, verification, and validation are applied on all transactions.

---

### 7. Dashboard & Reports

Provides statistical endpoints and downloadable reports for both system admins and individual restaurants.

#### System Admin

| Method | Endpoint | Description |
|---|---|---|
| GET | `/admin/stats/restaurants/count` | Count restaurants |
| GET | `/admin/stats/customers/count` | Count customers |
| GET | `/admin/stats/customers/active/count` | Count active customers |
| GET | `/admin/stats/orders/daily` | Daily orders count |
| GET | `/admin/stats/orders/monthly` | Monthly total orders |
| GET | `/admin/stats/orders/cancelled/daily` | Daily cancelled orders |
| GET | `/admin/stats/orders/cancelled/monthly` | Monthly cancelled orders |
| GET | `/admin/stats/transactions/daily` | Daily transactions (count & revenue) |
| GET | `/admin/stats/transactions/monthly` | Monthly transactions (count & revenue) |
| GET | `/admin/reports/transactions/daily` | Generate daily transactions report |
| GET | `/admin/reports/transactions/monthly` | Generate monthly transactions report |

#### Restaurant Owner

| Method | Endpoint | Description |
|---|---|---|
| GET | `/restaurants/{id}/stats/orders/daily` | Daily orders count |
| GET | `/restaurants/{id}/stats/orders/not-delivered/daily` | Daily orders not delivered count |
| GET | `/restaurants/{id}/stats/orders/monthly` | Monthly total orders count |
| GET | `/restaurants/{id}/stats/orders/cancelled/daily` | Daily cancelled orders |
| GET | `/restaurants/{id}/stats/orders/cancelled/monthly` | Monthly cancelled orders |
| GET | `/restaurants/{id}/stats/transactions/daily` | Daily transactions (count & revenue) |
| GET | `/restaurants/{id}/stats/transactions/monthly` | Monthly transactions (count & revenue) |
| GET | `/restaurants/{id}/reports/transactions/daily` | Generate daily transactions report |
| GET | `/restaurants/{id}/reports/transactions/monthly` | Generate monthly transactions report |

---

## ER Diagram

![ER Diagram](resources/Food_Delivery_Schema.png)

# E-store

This is a comprehensive mobile commerce (m-commerce) application that enables users to browse products from various vendors, add/remove items from their shopping cart, and complete their shopping cycle online. The app integrates Shopify's API for product data and provides seamless user authentication, shopping cart management, and payment processing.

## Table of Contents
- [Features](#features)
- [Features](#screenshots)
- [Technical Details](#technical-details)
  - [Architectural Pattern](#architectural-pattern)
  - [Libraries & Tools](#libraries--tools)
- [Authentication](#authentication)
- [API Integration](#api-integration)
  - [Shopify API](#shopify-api)
  - [Currency Conversion API](#currency-conversion-api)
  - [Google Maps API](#google-maps-api)
  - [Google Places API / Here API](#google-places-api--here-api)
  - [GeoNames API](#geonames-api)
- [UI Overview](#ui-overview)
  - [Home Tab](#home-tab)
  - [Categories Tab](#categories-tab)
  - [Shopping Cart Tab](#shopping-cart-tab)
  - [Profile Tab](#profile-tab)
- [Shopping Flow](#shopping-flow)
- [Payment Methods](#payment-methods)
- [Project Management](#project-management)
- [Team Members](#team-members)

## Features

### General Features
- **Product Browsing:** Browse products from various brands displayed in a grid view.
- **Search Functionality:** Search for specific products across all brands and categories.
- **Favorites & Wishlist:** Mark products as favorites and manage your wishlist.
- **Filter & Sorting:** Filter products by categories, price, and popularity (e.g., best-sellers).
- **Product Details:** View detailed information for each product, including images, price, ratings, available sizes, and descriptions.
- **Shopping Cart:** Add/remove items to/from the cart, adjust quantities, and proceed to checkout.
- **Checkout Process:** Provide a seamless checkout process with multiple payment options.
- **Address Management:** Manage shipping addresses using the Google Places or Here API for accurate location services.
- **User Authentication:** Sign up, login, and authenticate users with Firebase.
- **Order History:** View past orders.
- **Payment Options:** Cash on Delivery (with restrictions) and Online Payment. Users can apply discount codes during checkout.

## Screenshots


## App Screenshots

<table>
  <tr>
    <td><b>Home 1</b></td>
    <td><b>Home 2</b></td>
    <td><b>Categories</b></td>
    <td><b>Filters</b></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/877cc944-9719-4b29-99a8-f340fcae82f6" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/abe36aa7-61a3-44b7-9922-6563ba9e9420" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/d827c9ca-2b7a-489f-b0e8-2e53fd7fa281" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/86512b61-3738-4251-8fe6-582515ad4978" width="200"/></td>
  </tr>
  <tr>
    <td><b>Brand Details</b></td>
    <td><b>Need Login</b></td>
    <td><b>Guest Profile</b></td>
    <td><b>Sign Up</b></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/f4457899-9c48-4ffb-b4c7-7b05035a3083" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/58194e35-427f-4e1d-81ce-d08d1acd0b95" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/00a1892f-c467-4545-89db-09c663e4959f" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/b13b8d9a-e017-43a4-8294-c573a785b3ac" width="200"/></td>
  </tr>
  <tr>
    <td><b>Sign In</b></td>
    <td><b>Currency</b></td>
    <td><b>Item Details 1</b></td>
    <td><b>Item Details 2</b></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/3fbc6e93-1576-459a-a8bc-b92e27b6eec1" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/c72c1245-7c84-4e7a-84b3-230b94938b4b" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/75887201-47a3-46b6-b4ef-2a097b6e8f19" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/807c0629-e6d0-455b-99ef-96279f19f22f" width="200"/></td>
  </tr>
  <tr>
    <td><b>Favorites</b></td>
    <td><b>Location</b></td>
    <td><b>Cart</b></td>
    <td><b>Checkout</b></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/a1fa6058-5302-482d-99b0-c423840e1e02" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/90185842-4d22-4375-a4bc-4fca83972fa6" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/9f972bad-68bb-4ec5-b71e-29e21ffdda24" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/1b76852b-9b47-4094-8936-3a3b0760b98d" width="200"/></td>
  </tr>
  <tr>
    <td><b>Payment</b></td>
    <td><b>Success</b></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/2ce669dc-ae10-488c-b27f-c4386e218705" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/668b9edf-10fa-4621-82f9-44d442eb60cf" width="200"/></td>
  </tr>
</table>



## Technical Details

### Architectural Pattern
The app follows the **MVVM (Model-View-ViewModel)** architectural pattern to ensure a clear separation of concerns, improved testability, and better state management:
- **Model:** Handles data sources (APIs, databases) and provides data to the ViewModel.
- **View:** Displays data to the user, interacting with the ViewModel.
- **ViewModel:** Acts as an intermediary between the Model and View, managing UI-related data and handling logic.

### Libraries & Tools
- **Retrofit:** For API communication with Shopify and other services.
- **Firebase:** For authentication (Email/Password, Social Login) and email verification.
- **Coil:** For loading product images.
- **Navigation Component:** To manage navigation between screens.
- **JUnit:** For unit testing.

## Authentication

Users can authenticate via:
- **Email/Password:** Traditional authentication using Firebase with email verification.
- **Guest Mode:** Users can browse products but must sign in to use the shopping cart and wishlist functionalities.

## API Integration

### Shopify API
- Retrieves product information, pricing, and inventory data from Shopify.

### Currency Conversion API
- Fetches real-time currency exchange rates to show prices in different currencies.
- Used for handling payment calculations in the user’s preferred currency.

### Google Maps API
- Provides map-based functionality for location-based services.

### Google Places API / Here API
- Used for address auto-completion and location-based services for shipping details.
- Ensures the user enters valid addresses before proceeding to checkout.

### GeoNames API
- Provides geolocation-based services like reverse geocoding, helping in identifying user locations.

## UI Overview

### Home Tab
- **Search Bar:** Search for products by name or category.
- **Featured Ads:** Scrollable banner for displaying promotional offers.
- **Brands Section:** Scrollable list of brands.
- **Product Grid:** Displays available products with options to add to cart or mark as favorite.
- **Navigation Options:**
  - Search
  - Favorites

### Categories Tab
- **Main Categories:** Displays a list of product categories (e.g., Electronics, Fashion).
- **Subcategories:** Allows users to drill down into specific subcategories.
- **Product Grid:** Displays products filtered by category.

### Shopping Cart Tab
- **Cart Summary:** Displays all items added to the shopping cart, allows adjusting quantities and removing items.
- **Checkout Button:** Proceeds to the checkout process.

### Profile Tab
- **Logged In Users:**
  - Welcome message with the user's name.
  - Buttons to navigate to order history, wishlist, addresses, settings, and sign out.
- **Guest Users:**
  - Buttons to sign in or register.

## Shopping Flow
1. Browse products from brands or categories.
2. View product details and add items to cart.
3. Manage cart: adjust quantities, remove items, view total cost.
4. Checkout:
   - Select payment method: Cash on Delivery or Online Payment.
   - Apply discount codes.
   - Confirm order and send email confirmation.

## Payment Methods
- **Cash on Delivery (COD):** Available for orders under a specified limit. Users must provide an address and phone number.
- **Online Payment:** Develpoed payment operations to complete order placment.
- **Discounts:** Users can apply promo codes at checkout.

## Project Management
- **Trello Board Link:** [Trello Board](https://trello.com/b/lXZTQoQQ/e-store-app-team3)
  - Tracks progress, task assignments, and issue management.
- **GitHup Project Link:** [GitHup Repo](https://github.com/MahmooudDarwish/E-Store)
  - Code versions hosting

## Team Members

| Name                | Task Contributions                                           |
|---------------------|--------------------------------------------------------------|
| Mahmoud Darwish     | Brands, Categories, Orders, Home Design                      |
| Kareem Ashraf       | Authentication, Product Info, Search, Favorites              |
| Muhammed Abdulrahim | Coupons (Discount code), Shopping Cart, Payment, Ads, Settings |

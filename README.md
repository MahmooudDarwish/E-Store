# E-store

This is a comprehensive mobile commerce (m-commerce) application that enables users to browse products from various vendors, add/remove items from their shopping cart, and complete their shopping cycle online. The app integrates Shopify's API for product data and provides seamless user authentication, shopping cart management, and payment processing.

## Table of Contents
- [Features](#features)
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

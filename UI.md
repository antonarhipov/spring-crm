# CRM System UI Guidelines

## Table of Contents
1. [Design System](#design-system)
2. [Layout Guidelines](#layout-guidelines)
3. [Typography](#typography)
4. [Color Scheme](#color-scheme)
5. [Components](#components)
6. [Form Design](#form-design)
7. [Navigation](#navigation)
8. [Responsive Design](#responsive-design)
9. [Accessibility](#accessibility)
10. [Error Handling](#error-handling)
11. [Loading States](#loading-states)
12. [Interactive Elements](#interactive-elements)
13. [Best Practices](#best-practices)

## Design System

### Core Principles
- Consistency
- Simplicity
- Efficiency
- User-Centric Design
- Accessibility First

## Layout Guidelines

### Grid System
- Use a 12-column grid system
- Maintain consistent spacing using multiples of 8px
- Standard margins: 16px, 24px, 32px
- Standard padding: 16px, 24px

### Page Structure
```html
<div class="page-container">
    <header class="header">
        <!-- Navigation and global actions -->
    </header>
    
    <aside class="sidebar">
        <!-- Secondary navigation -->
    </aside>
    
    <main class="main-content">
        <!-- Primary content -->
    </main>
</div>
```

### Responsive Breakpoints
- Mobile: < 768px
- Tablet: 768px - 1024px
- Desktop: > 1024px

## Typography

### Font Family
- Primary: Inter
- Secondary: Roboto
- Monospace: JetBrains Mono (for code examples)

### Font Sizes
```css
:root {
    --font-size-xs: 12px;
    --font-size-sm: 14px;
    --font-size-base: 16px;
    --font-size-lg: 18px;
    --font-size-xl: 24px;
    --font-size-2xl: 32px;
}
```

### Font Weights
- Regular: 400
- Medium: 500
- Semi-bold: 600
- Bold: 700

## Color Scheme

### Primary Colors
```css
:root {
    --primary-50: #E3F2FD;
    --primary-100: #BBDEFB;
    --primary-500: #2196F3;
    --primary-700: #1976D2;
    --primary-900: #0D47A1;
}
```

### Secondary Colors
```css
:root {
    --secondary-50: #F3E5F5;
    --secondary-100: #E1BEE7;
    --secondary-500: #9C27B0;
    --secondary-700: #7B1FA2;
    --secondary-900: #4A148C;
}
```

### Neutral Colors
```css
:root {
    --neutral-50: #FAFAFA;
    --neutral-100: #F5F5F5;
    --neutral-200: #EEEEEE;
    --neutral-300: #E0E0E0;
    --neutral-400: #BDBDBD;
    --neutral-500: #9E9E9E;
    --neutral-600: #757575;
    --neutral-700: #616161;
    --neutral-800: #424242;
    --neutral-900: #212121;
}
```

### Semantic Colors
```css
:root {
    --success: #4CAF50;
    --warning: #FFC107;
    --error: #F44336;
    --info: #2196F3;
}
```

## Components

### Buttons

#### Primary Button
```html
<button class="btn btn-primary">
    Primary Action
</button>
```

```css
.btn-primary {
    background-color: var(--primary-500);
    color: white;
    padding: 8px 16px;
    border-radius: 4px;
    font-weight: 500;
}
```

#### Secondary Button
```html
<button class="btn btn-secondary">
    Secondary Action
</button>
```

#### Text Button
```html
<button class="btn btn-text">
    Text Action
</button>
```

### Cards
```html
<div class="card">
    <div class="card-header">
        <h3>Card Title</h3>
    </div>
    <div class="card-content">
        <!-- Content -->
    </div>
    <div class="card-footer">
        <!-- Actions -->
    </div>
</div>
```

### Tables
```html
<table class="table">
    <thead>
        <tr>
            <th>Header</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>Content</td>
        </tr>
    </tbody>
</table>
```

## Form Design

### Input Fields
```html
<div class="form-group">
    <label for="input">Label</label>
    <input type="text" id="input" class="form-control" />
    <span class="helper-text">Helper text</span>
</div>
```

### Validation States
```css
.form-control.is-valid {
    border-color: var(--success);
}

.form-control.is-invalid {
    border-color: var(--error);
}
```

### Form Layout
- Single column layout for mobile
- Two columns for desktop where appropriate
- Group related fields
- Clear section headings
- Consistent spacing between fields (24px)

## Navigation

### Main Navigation
```html
<nav class="main-nav">
    <ul>
        <li><a href="/dashboard">Dashboard</a></li>
        <li><a href="/customers">Customers</a></li>
        <li><a href="/orders">Orders</a></li>
    </ul>
</nav>
```

### Breadcrumbs
```html
<nav aria-label="breadcrumb">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="/">Home</a></li>
        <li class="breadcrumb-item active">Current Page</li>
    </ol>
</nav>
```

## Responsive Design

### Mobile-First Approach
```css
/* Base styles (mobile) */
.container {
    padding: 16px;
}

/* Tablet styles */
@media (min-width: 768px) {
    .container {
        padding: 24px;
    }
}

/* Desktop styles */
@media (min-width: 1024px) {
    .container {
        padding: 32px;
        max-width: 1200px;
        margin: 0 auto;
    }
}
```

## Accessibility

### ARIA Labels
```html
<button aria-label="Close dialog">
    <span class="icon">×</span>
</button>
```

### Focus States
```css
:focus {
    outline: 2px solid var(--primary-500);
    outline-offset: 2px;
}
```

### Color Contrast
- Minimum contrast ratio of 4.5:1 for normal text
- Minimum contrast ratio of 3:1 for large text

## Error Handling

### Error Messages
```html
<div class="alert alert-error" role="alert">
    <span class="alert-icon">!</span>
    <p class="alert-message">Error message here</p>
</div>
```

### Form Validation
```html
<div class="form-group">
    <label for="email">Email</label>
    <input 
        type="email" 
        id="email" 
        class="form-control is-invalid"
        aria-describedby="email-error"
    />
    <span id="email-error" class="error-message">
        Please enter a valid email address
    </span>
</div>
```

## Loading States

### Spinner
```html
<div class="spinner" role="status">
    <span class="sr-only">Loading...</span>
</div>
```

### Skeleton Screens
```html
<div class="skeleton-loader">
    <div class="skeleton-line"></div>
    <div class="skeleton-line"></div>
    <div class="skeleton-line"></div>
</div>
```

## Interactive Elements

### Hover States
```css
.btn:hover {
    background-color: var(--primary-700);
    transform: translateY(-1px);
}
```

### Active States
```css
.btn:active {
    transform: translateY(1px);
}
```

## Best Practices

### Performance
- Optimize images
- Lazy load off-screen content
- Minimize CSS and JavaScript
- Use appropriate image formats

### Consistency
- Maintain consistent spacing
- Use design tokens
- Follow established patterns
- Document component variations

### User Experience
- Clear feedback for actions
- Intuitive navigation
- Meaningful error messages
- Progressive disclosure
- Appropriate loading indicators

### Code Quality
- Use semantic HTML
- Maintain clean CSS
- Follow BEM naming convention
- Document complex components
- Create reusable utilities

### Testing
- Cross-browser testing
- Responsive testing
- Accessibility testing
- Performance testing
- User testing
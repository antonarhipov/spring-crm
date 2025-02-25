import { createGlobalStyle } from 'styled-components'

export const GlobalStyle = createGlobalStyle`
  :root {
    /* Primary Colors */
    --primary-50: #E3F2FD;
    --primary-100: #BBDEFB;
    --primary-500: #2196F3;
    --primary-700: #1976D2;
    --primary-900: #0D47A1;

    /* Secondary Colors */
    --secondary-50: #F3E5F5;
    --secondary-100: #E1BEE7;
    --secondary-500: #9C27B0;
    --secondary-700: #7B1FA2;
    --secondary-900: #4A148C;

    /* Neutral Colors */
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

    /* Semantic Colors */
    --success: #4CAF50;
    --warning: #FFC107;
    --error: #F44336;
    --info: #2196F3;

    /* Font Sizes */
    --font-size-xs: 12px;
    --font-size-sm: 14px;
    --font-size-base: 16px;
    --font-size-lg: 18px;
    --font-size-xl: 24px;
    --font-size-2xl: 32px;
  }

  * {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
  }

  body {
    font-family: 'Inter', sans-serif;
    font-size: var(--font-size-base);
    line-height: 1.5;
    color: var(--neutral-900);
    background: var(--neutral-50);
  }

  a {
    color: var(--primary-500);
    text-decoration: none;
    &:hover {
      color: var(--primary-700);
    }
  }

  :focus {
    outline: 2px solid var(--primary-500);
    outline-offset: 2px;
  }
`
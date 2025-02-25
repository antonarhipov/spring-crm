import type { ThemeConfig } from 'antd'

export const theme: ThemeConfig = {
  token: {
    fontFamily: 'Inter, sans-serif',
    fontSize: 16,
    colorPrimary: '#2196F3',
    colorPrimaryHover: '#1976D2',
    colorPrimaryActive: '#1565C0',
    colorSuccess: '#4CAF50',
    colorWarning: '#FFC107',
    colorError: '#F44336',
    colorTextBase: '#212121',
    colorBgContainer: '#FFFFFF',
    borderRadius: 4,
    wireframe: false,
    // Spacing
    padding: 16,
    paddingXS: 8,
    paddingLG: 24,
    margin: 16,
    marginXS: 8,
    marginLG: 24
  },
  components: {
    Button: {
      borderRadius: 4,
      controlHeight: 40,
      controlHeightLG: 48,
      controlHeightSM: 32,
      paddingContentHorizontal: 16
    },
    Input: {
      controlHeight: 40,
      controlHeightLG: 48,
      controlHeightSM: 32,
      padding: 16
    },
    Select: {
      controlHeight: 40,
      controlHeightLG: 48,
      controlHeightSM: 32
    },
    Card: {
      padding: 24,
      borderRadiusLG: 8
    },
    Table: {
      borderRadius: 8,
      padding: 16,
      headerBg: '#F5F5F5'
    },
    Layout: {
      headerBg: '#FFFFFF',
      headerHeight: 64,
      siderBg: '#FFFFFF',
      triggerBg: '#FFFFFF'
    }
  }
}

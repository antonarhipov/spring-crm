import { useState } from 'react'
import styled from 'styled-components'
import { Layout } from 'antd'
import { Header } from '../Header'
import { Sidebar } from '../Sidebar'

const { Content } = Layout

const StyledLayout = styled(Layout)`
  min-height: 100vh;
`

const MainContent = styled(Content)`
  margin-left: 250px;
  margin-top: 64px;
  padding: 24px;
  background: var(--neutral-50);
  min-height: calc(100vh - 64px);
  transition: margin-left 0.2s;

  &.sidebar-collapsed {
    margin-left: 80px;
  }
`

interface MainLayoutProps {
  children: React.ReactNode
}

export const MainLayout = ({ children }: MainLayoutProps) => {
  const [collapsed, setCollapsed] = useState(false)

  const handleCollapse = (value: boolean) => {
    setCollapsed(value)
  }

  return (
    <StyledLayout>
      <Header />
      <Layout>
        <Sidebar collapsed={collapsed} onCollapse={handleCollapse} />
        <MainContent className={collapsed ? 'sidebar-collapsed' : ''}>
          {children}
        </MainContent>
      </Layout>
    </StyledLayout>
  )
}

export default MainLayout

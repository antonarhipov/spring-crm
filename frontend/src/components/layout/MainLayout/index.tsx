import React from 'react'
import { Layout } from 'antd'
import { Outlet } from 'react-router-dom'
import styled from 'styled-components'
import Header from '../Header'
import Sidebar from '../Sidebar'

const { Content } = Layout

const StyledLayout = styled(Layout)`
  min-height: 100vh;
`

const StyledContent = styled(Content)`
  margin: 24px;
  padding: 24px;
  background: #fff;
  border-radius: 8px;
  min-height: auto;
`

const MainLayout: React.FC = () => {
  const [collapsed, setCollapsed] = React.useState(false)

  const toggleSidebar = () => {
    setCollapsed(!collapsed)
  }

  return (
    <StyledLayout>
      <Sidebar collapsed={collapsed} />
      <Layout>
        <Header collapsed={collapsed} onToggle={toggleSidebar} />
        <StyledContent>
          <Outlet />
        </StyledContent>
      </Layout>
    </StyledLayout>
  )
}

export default MainLayout
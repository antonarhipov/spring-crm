import React from 'react'
import { Layout, Menu } from 'antd'
import { useLocation, useNavigate } from 'react-router-dom'
import { useSelector } from 'react-redux'
import {
  DashboardOutlined,
  TeamOutlined,
  UserOutlined,
  SettingOutlined
} from '@ant-design/icons'
import styled from 'styled-components'
import { RootState } from '@store/index'
import { UserRole } from '@models'

const { Sider } = Layout

interface SidebarProps {
  collapsed: boolean
}

const StyledSider = styled(Sider)`
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);

  .ant-layout-sider-children {
    display: flex;
    flex-direction: column;
    height: 100vh;
  }
`

const Logo = styled.div`
  height: 64px;
  padding: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fff;
  border-bottom: 1px solid #f0f0f0;

  img {
    height: 32px;
    transition: all 0.2s;
  }
`

const Sidebar: React.FC<SidebarProps> = ({ collapsed }) => {
  const navigate = useNavigate()
  const location = useLocation()
  const { user } = useSelector((state: RootState) => state.auth)
  const isAdmin = user.roles.includes(UserRole.ADMIN)

  const menuItems = [
    {
      key: '/dashboard',
      icon: <DashboardOutlined />,
      label: 'Dashboard'
    },
    {
      key: '/customers',
      icon: <TeamOutlined />,
      label: 'Customers'
    },
    ...(isAdmin ? [
      {
        key: '/users',
        icon: <UserOutlined />,
        label: 'Users'
      }
    ] : []),
    {
      key: '/settings',
      icon: <SettingOutlined />,
      label: 'Settings'
    }
  ]

  const handleMenuClick = ({ key }: { key: string }) => {
    navigate(key)
  }

  return (
    <StyledSider
      trigger={null}
      collapsible
      collapsed={collapsed}
      width={256}
    >
      <Logo>
        <img
          src="/logo.svg"
          alt="CRM Logo"
          style={{ width: collapsed ? '32px' : '120px' }}
        />
      </Logo>
      <Menu
        mode="inline"
        selectedKeys={[location.pathname]}
        items={menuItems}
        onClick={handleMenuClick}
      />
    </StyledSider>
  )
}

export default Sidebar

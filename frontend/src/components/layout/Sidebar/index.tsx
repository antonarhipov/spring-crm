import { useState } from 'react'
import styled from 'styled-components'
import { Layout, Menu } from 'antd'
import { Link, useLocation } from 'react-router-dom'
import {
  DashboardOutlined,
  TeamOutlined,
  ShoppingCartOutlined,
  BarChartOutlined,
  SettingOutlined,
  MenuFoldOutlined,
  MenuUnfoldOutlined,
} from '@ant-design/icons'

const { Sider: AntSider } = Layout

const StyledSider = styled(AntSider)`
  background: white;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.05);
  position: fixed;
  height: 100vh;
  left: 0;
  top: 64px;
  z-index: 100;

  .ant-layout-sider-children {
    display: flex;
    flex-direction: column;
  }

  .ant-menu {
    border-right: none;
  }
`

const CollapseButton = styled.button`
  position: absolute;
  right: -12px;
  top: 24px;
  width: 24px;
  height: 24px;
  background: white;
  border: 1px solid var(--neutral-200);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: var(--neutral-600);
  z-index: 1;

  &:hover {
    color: var(--primary-500);
    border-color: var(--primary-500);
  }
`

const menuItems = [
  {
    key: 'dashboard',
    icon: <DashboardOutlined />,
    label: <Link to="/">Dashboard</Link>,
  },
  {
    key: 'customers',
    icon: <TeamOutlined />,
    label: <Link to="/customers">Customers</Link>,
  },
  {
    key: 'orders',
    icon: <ShoppingCartOutlined />,
    label: <Link to="/orders">Orders</Link>,
  },
  {
    key: 'reports',
    icon: <BarChartOutlined />,
    label: <Link to="/reports">Reports</Link>,
  },
  {
    type: 'divider',
  },
  {
    key: 'settings',
    icon: <SettingOutlined />,
    label: <Link to="/settings">Settings</Link>,
  },
]

interface SidebarProps {
  collapsed: boolean
  onCollapse: (collapsed: boolean) => void
}

export const Sidebar = ({ collapsed, onCollapse }: SidebarProps) => {
  const location = useLocation()

  const selectedKey = menuItems.find(
    item => item.key && location.pathname.startsWith(`/${item.key}`)
  )?.key || 'dashboard'

  return (
    <StyledSider
      width={250}
      collapsible
      collapsed={collapsed}
      trigger={null}
    >
      <Menu
        mode="inline"
        selectedKeys={[selectedKey]}
        style={{ padding: '24px 0' }}
        items={menuItems}
      />
      <CollapseButton onClick={() => onCollapse(!collapsed)}>
        {collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
      </CollapseButton>
    </StyledSider>
  )
}

export default Sidebar

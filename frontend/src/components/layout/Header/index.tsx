import styled from 'styled-components'
import { Layout, Menu, Avatar, Dropdown } from 'antd'
import { UserOutlined, BellOutlined, SettingOutlined } from '@ant-design/icons'
import { Link } from 'react-router-dom'

const { Header: AntHeader } = Layout

const StyledHeader = styled(AntHeader)`
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  height: 64px;
  position: fixed;
  width: 100%;
  z-index: 1000;
`

const Logo = styled(Link)`
  font-size: var(--font-size-xl);
  font-weight: 700;
  color: var(--primary-500);
  text-decoration: none;
  margin-right: 48px;
`

const HeaderRight = styled.div`
  display: flex;
  align-items: center;
  gap: 24px;
`

const IconButton = styled.button`
  background: none;
  border: none;
  cursor: pointer;
  padding: 8px;
  color: var(--neutral-600);
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  
  &:hover {
    background: var(--neutral-50);
    color: var(--neutral-900);
  }
`

const userMenuItems = [
  {
    key: 'profile',
    label: 'Profile',
    icon: <UserOutlined />,
  },
  {
    key: 'settings',
    label: 'Settings',
    icon: <SettingOutlined />,
  },
  {
    type: 'divider',
  },
  {
    key: 'logout',
    label: 'Logout',
    danger: true,
  },
]

export const Header = () => {
  return (
    <StyledHeader>
      <div style={{ display: 'flex', alignItems: 'center' }}>
        <Logo to="/">CRM System</Logo>
        <Menu
          mode="horizontal"
          style={{ border: 'none', lineHeight: '64px' }}
          items={[
            { key: 'dashboard', label: <Link to="/">Dashboard</Link> },
            { key: 'customers', label: <Link to="/customers">Customers</Link> },
            { key: 'orders', label: <Link to="/orders">Orders</Link> },
          ]}
        />
      </div>
      <HeaderRight>
        <IconButton>
          <BellOutlined style={{ fontSize: '20px' }} />
        </IconButton>
        <Dropdown
          menu={{ items: userMenuItems }}
          placement="bottomRight"
          trigger={['click']}
        >
          <Avatar
            style={{ cursor: 'pointer', backgroundColor: 'var(--primary-500)' }}
            icon={<UserOutlined />}
          />
        </Dropdown>
      </HeaderRight>
    </StyledHeader>
  )
}

export default Header
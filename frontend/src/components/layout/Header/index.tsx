import React from 'react'
import { Layout, Button, Dropdown, Space, Avatar } from 'antd'
import { MenuFoldOutlined, MenuUnfoldOutlined, UserOutlined, LogoutOutlined, SettingOutlined } from '@ant-design/icons'
import { useNavigate } from 'react-router-dom'
import { useSelector, useDispatch } from 'react-redux'
import styled from 'styled-components'
import { RootState } from '@store/index'
import { logout } from '@store/slices/authSlice'

const { Header: AntHeader } = Layout

interface HeaderProps {
  collapsed: boolean
  onToggle: () => void
}

const StyledHeader = styled(AntHeader)`
  padding: 0 24px;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
  z-index: 1;
`

const LeftSection = styled.div`
  display: flex;
  align-items: center;
`

const RightSection = styled.div`
  display: flex;
  align-items: center;
  gap: 16px;
`

const UserName = styled.span`
  margin-right: 8px;
  font-weight: 500;
`

const Header: React.FC<HeaderProps> = ({ collapsed, onToggle }) => {
  const navigate = useNavigate()
  const dispatch = useDispatch()
  const { user } = useSelector((state: RootState) => state.auth)

  const handleLogout = () => {
    dispatch(logout())
    navigate('/login')
  }

  const userMenuItems = [
    {
      key: 'settings',
      label: 'Settings',
      icon: <SettingOutlined />,
      onClick: () => navigate('/settings')
    },
    {
      key: 'logout',
      label: 'Logout',
      icon: <LogoutOutlined />,
      onClick: handleLogout
    }
  ]

  return (
    <StyledHeader>
      <LeftSection>
        <Button
          type="text"
          icon={collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
          onClick={onToggle}
          style={{ fontSize: '16px', width: 64, height: 64 }}
        />
      </LeftSection>
      <RightSection>
        <Dropdown menu={{ items: userMenuItems }} placement="bottomRight">
          <Space style={{ cursor: 'pointer' }}>
            <Avatar icon={<UserOutlined />} />
            <UserName>{user.username}</UserName>
          </Space>
        </Dropdown>
      </RightSection>
    </StyledHeader>
  )
}

export default Header
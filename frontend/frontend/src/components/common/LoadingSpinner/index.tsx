import React from 'react'
import { Spin } from 'antd'
import { LoadingOutlined } from '@ant-design/icons'
import styled from 'styled-components'

const SpinnerContainer = styled.div`
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
`

const LoadingSpinner: React.FC = () => (
  <SpinnerContainer>
    <Spin indicator={<LoadingOutlined style={{ fontSize: 24 }} spin />} />
  </SpinnerContainer>
)

export default LoadingSpinner
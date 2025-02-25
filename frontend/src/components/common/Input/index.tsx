import styled from 'styled-components'
import { Input as AntInput } from 'antd'
import type { InputProps } from 'antd'

interface StyledInputProps extends InputProps {
  error?: string
  helperText?: string
}

const InputWrapper = styled.div`
  display: flex;
  flex-direction: column;
  gap: 4px;
`

const Label = styled.label`
  color: var(--neutral-900);
  font-size: var(--font-size-sm);
  font-weight: 500;
`

const StyledInput = styled(AntInput)<{ $hasError?: boolean }>`
  height: 40px;
  padding: 8px 12px;
  border-radius: 4px;
  border: 1px solid ${props => props.$hasError ? 'var(--error)' : 'var(--neutral-300)'};
  background-color: white;
  
  &:hover {
    border-color: ${props => props.$hasError ? 'var(--error)' : 'var(--primary-500)'};
  }
  
  &:focus {
    border-color: ${props => props.$hasError ? 'var(--error)' : 'var(--primary-500)'};
    box-shadow: 0 0 0 2px ${props => props.$hasError ? 'rgba(244, 67, 54, 0.2)' : 'rgba(33, 150, 243, 0.2)'};
  }
`

const HelperText = styled.span<{ $error?: boolean }>`
  font-size: var(--font-size-xs);
  color: ${props => props.$error ? 'var(--error)' : 'var(--neutral-600)'};
`

export const Input = ({ 
  label,
  error,
  helperText,
  id,
  ...props 
}: StyledInputProps & { label?: string }) => {
  return (
    <InputWrapper>
      {label && <Label htmlFor={id}>{label}</Label>}
      <StyledInput
        id={id}
        $hasError={!!error}
        status={error ? 'error' : undefined}
        {...props}
      />
      {(error || helperText) && (
        <HelperText $error={!!error}>
          {error || helperText}
        </HelperText>
      )}
    </InputWrapper>
  )
}

export default Input
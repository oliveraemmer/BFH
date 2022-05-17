import '../styles/globals.css'
import Layout from '../components/Layout'
import { ThemeProvider, createTheme } from '@mui/material'
import { AuthProvider } from '../contexts/Auth'

const theme = createTheme({
  palette: {
    primary: {
      main: '#ffab91',
    },
    secondary: {
      main: '#9575cd',
    },
  },
})

function MyApp({ Component, pageProps }) {

  return (
    <AuthProvider>
      <ThemeProvider theme={theme}>
        <Layout>
          <Component {...pageProps} />
        </Layout>
      </ThemeProvider>
    </AuthProvider>
  )
}

export default MyApp

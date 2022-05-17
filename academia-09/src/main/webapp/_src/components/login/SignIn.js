import * as React from 'react';
import { useRouter } from 'next/router';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { useAuth } from '../../contexts/Auth';
import Swal from 'sweetalert2';
import { CircularProgress, Skeleton } from '@mui/material';
import {errorMessage} from "../message/Message";
import LinearProgress from '@material-ui/core/LinearProgress';

export default function SignIn() {
  const authenticate = useAuth();
  const router = useRouter();
  const [loading, setLoading] = React.useState(false);
  const [usernameError, setUsernameError] = React.useState(false);
  const [passwordError, setPasswordError] = React.useState(false);


  const handleSubmit = async (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    const username = data.get('username');
    const password = data.get('password');
    // eslint-disable-next-line no-console


    username == "" ? setUsernameError(true) : setUsernameError(false)
    password == "" ? setPasswordError(true) : setPasswordError(false)
    if(username != "" && password != ""){
      try {
        setLoading(true);
        await authenticate.login(username, password);
        setLoading(authenticate.loading);
      } catch (error) {
        console.log(error.response.status)
        if(error.response.status == 500){
          errorMessage.text = 'Internal Server Error'
          await Swal.fire(errorMessage);
        } else if (error.response.status == 401){
          errorMessage.text = 'Invalid username or password'
          await Swal.fire(errorMessage);
        } else if (error.response.status == 400){
          errorMessage.text = 'Bad request'
          await Swal.fire(errorMessage);
        }
        setLoading(false);
      }
    }
  };

  if (authenticate.user) {
    router.push('/overview');
  }

  return (
    <Container component="main" maxWidth="xs">
      {authenticate.user ?
        <Box
          display="flex"
          flexDirection="column"
          alignItems="center"
          justifyContent="center"
        >
          <CircularProgress />
        </Box>
        :
        <Box
          sx={{
            marginTop: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <Avatar sx={{ m: 1, bgcolor: 'primary.main' }}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Sign in
          </Typography>
          <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
            <TextField
                error = {usernameError}
              margin="normal"
              required
              fullWidth
              id="username"
              label="Username"
              name="username"
              autoComplete="text"
              autoFocus
            />
            <TextField
                error = {passwordError}
              margin="normal"
              required
              fullWidth
              name="password"
              label="Password"
              type="password"
              id="password"
              autoComplete="current-password"
            />
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              Sign In
            </Button>
          </Box>
        </Box>

      }
    </Container>
  );
}
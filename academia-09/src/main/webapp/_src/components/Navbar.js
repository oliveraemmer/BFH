import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import Menu from '@mui/material/Menu';
import Container from '@mui/material/Container';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import MenuItem from '@mui/material/MenuItem';
import Link from 'next/link';
import { useAuth } from '../contexts/Auth';
import { useRouter } from 'next/router';

let pages = [];
let settings = [];
let avatar = () => { };
let mobileMenu = () => { };

const ResponsiveAppBar = ({ person }) => {
    const [anchorElNav, setAnchorElNav] = React.useState(null);
    const [anchorElUser, setAnchorElUser] = React.useState(null);
    const authenticate = useAuth();
    const router = useRouter();

    const handleOpenUserMenu = (event) => {
        setAnchorElUser(event.currentTarget);
    };

    const handleCloseNavMenu = () => {
        setAnchorElNav(null);
    };

    const handleCloseUserMenu = () => {
        setAnchorElUser(null);
    };

    const handleLogout = () => {
        if (authenticate.user) {
            settings = [];
            avatar = () => { };
            authenticate.logout();
            router.push('/');
        }
        handleCloseUserMenu();
    }

    const handleProfile = () => {
        router.push('/profile');
        handleCloseUserMenu();
    }

    if (authenticate.user) {
        settings = [
            {
                type: "profile",
                text: <Typography fontWeight="light">{authenticate.person ? authenticate.person["firstname"] + " " + authenticate.person["lastname"] : "Loading..."}</Typography>,
                onClick: handleProfile
            },
            {
                type: "signout",
                text: "Sign Out",
                onClick: handleLogout
            }
        ]


        if (authenticate.user["role"] === "teacher") {
            avatar = (
                <IconButton onClick={handleOpenUserMenu} sx={{ p: 0 }}>
                    <Avatar alt={authenticate.user["username"]} src="/images/avatar/teacher.png" />
                </IconButton>
            )
        } else {
            avatar = (
                <IconButton onClick={handleOpenUserMenu} sx={{ p: 0 }}>
                    <Avatar alt={authenticate.user["username"]} src="/images/avatar/student.png" />
                </IconButton>
            )
        }
    }

    return (
        <AppBar position="static">
            <Container maxWidth="xl">
                <Toolbar disableGutters>
                    <Typography
                        variant="h6"
                        noWrap
                        component="div"
                        sx={{ mr: 2, display: { xs: 'none', md: 'flex' } }}
                    >

                        <Link href="/" passHref={true}>
                            <img src='./logo_color.png' width="40" height="40" alt="logo" />
                        </Link>
                    </Typography>

                    <Box sx={{ flexGrow: 1, display: { xs: 'flex', md: 'none' } }}>

                        {mobileMenu()}

                    </Box>
                    <Typography
                        variant="h6"
                        noWrap
                        component="div"
                        sx={{ flexGrow: 1, display: { xs: 'flex', md: 'none' } }}
                    >
                        <Link href="/" passHref={true}>
                            <img src='./logo_color.png' width="40" height="40" alt="logo" />
                        </Link>
                    </Typography>
                    <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' } }}>
                        {pages.map((page) => (
                            <Button
                                key={page}
                                onClick={handleCloseNavMenu}
                                sx={{ my: 2, color: 'white', display: 'block' }}
                            >
                                {page}
                            </Button>
                        ))}
                    </Box>

                    <Box sx={{ flexGrow: 0 }}>
                        {avatar}
                        <Menu
                            sx={{ mt: '45px' }}
                            id="menu-appbar"
                            anchorEl={anchorElUser}
                            anchorOrigin={{
                                vertical: 'top',
                                horizontal: 'right',
                            }}
                            keepMounted
                            transformOrigin={{
                                vertical: 'top',
                                horizontal: 'right',
                            }}
                            open={Boolean(anchorElUser)}
                            onClose={handleCloseUserMenu}
                        >
                            {settings.map((setting) => (
                                <MenuItem key={setting.type} onClick={setting.onClick}>
                                    <Typography textAlign="center">{setting.text}</Typography>
                                </MenuItem>
                            ))}
                        </Menu>
                    </Box>
                </Toolbar>
            </Container>
        </AppBar>
    );
};
export default ResponsiveAppBar;


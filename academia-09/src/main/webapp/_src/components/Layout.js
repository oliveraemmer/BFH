import Navbar from './Navbar'
import { Avatar, Badge, Box, Grid, Link, List, Typography, ListItem, ListItemAvatar, ListItemText, Divider } from '@mui/material'
import React from 'react'
import Footer from './Footer'

const name = 'Academia'

export default function Layout(props) {

    const App = () => (
        <Box
            flex={1}
            paddingBottom={3}
        >
            <Navbar />
            <Box
                paddingTop="10%"
                paddingBottom="10%"
            >
                {React.cloneElement(props.children, { name })}
            </Box>
        </Box>
    )

    return (
        <Box
            display={'flex'}
            flexDirection={'column'}
            height={'100vh'}
        >
            <App />
            <Footer name={name} />
        </Box>
    )

}

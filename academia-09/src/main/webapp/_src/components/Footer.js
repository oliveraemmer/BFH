import { Avatar, Box, Grid, Link, List, Typography, ListItem, ListItemAvatar, ListItemText, Divider } from '@mui/material'

export default function Footer({ name }) {

    const Members1 = [
        {
            name: 'Jan Lauber',
            image: '/images/avatar/contributors/janlauber.jpeg',
            githubUrl: 'https://github.com/janlauber',
        },
        {
            name: 'Jan Fuhrer',
            image: '/images/avatar/contributors/janfuhrer.png',
            githubUrl: 'https://github.com/janfuhrer',
        },
    ]

    const Members2 = [
        {
            name: 'Oliver Aemmer',
            image: '/images/avatar/contributors/oliveraemmer.jpeg',
            githubUrl: 'https://github.com/oliveraemmer',
        },
        {
            name: 'Mario Hadorn',
            image: '/images/avatar/contributors/mariohadorn.png',
            githubUrl: 'https://github.com/mariohadorn',
        },
    ]

    return (
        <Box
            p={2}
            justifyContent="center"
            alignItems="center"
            display="flex"
            flexDirection="column"
            textAlign="center"
            bgcolor="#f5f5f5"
        >
            <Box
                sx={{
                    fontSize: '0.8rem',
                    color: '#9e9e9e',
                }}
            >
                <Typography color="text">
                    &copy; {new Date().getFullYear()} {name}
                </Typography>

                <Box>
                    <Typography color="primary">
                        Contributions by
                    </Typography>
                </Box>

                <Grid
                    container
                    direction={'row'}
                    justify={'center'}
                    alignItems={'center'}
                >
                    <List>

                        {Members2.map((member, index) => (
                            <div key={index}>
                                {index > 0 ?
                                    <Divider variant="inset" component="li" />
                                    :
                                    null}

                                <Link href={member.githubUrl} target="_blank" underline="none">
                                    <ListItem>
                                        <ListItemAvatar>
                                            <Avatar>
                                                <img src={member.image} alt='Jan Lauber' width="100%" height="100%" />
                                            </Avatar>
                                        </ListItemAvatar>
                                        <ListItemText primary={member.name} secondary={member.githubUrl} />
                                    </ListItem>

                                </Link>
                            </div>
                        ))}
                    </List>
                    <List>

                        {Members1.map((member, index) => (
                            <div key={index}>
                                {index > 0 ?
                                    <Divider variant="inset" component="li" />
                                    :
                                    null}
                                <Link href={member.githubUrl} target="_blank" underline="none">
                                    <ListItem>
                                        <ListItemAvatar>
                                            <Avatar>
                                                <img src={member.image} alt='Jan Lauber' width="100%" height="100%" />
                                            </Avatar>
                                        </ListItemAvatar>
                                        <ListItemText primary={member.name} secondary={member.githubUrl} />
                                    </ListItem>
                                </Link>
                            </div>
                        ))}
                    </List>
                </Grid>

            </Box>
        </Box>
    )
}
import { useAuth } from "../contexts/Auth";
import { Avatar, Container, CssBaseline, Table, TableContainer, Typography, TableBody, TableRow, TableCell, Button, Link, Skeleton } from "@mui/material";
import { Box } from "@mui/system";
import ArrowBackIcon from '@mui/icons-material/ArrowBack';

export default function Profile() {
    const authenticate = useAuth();

    return (
        <Container component="main" maxWidth="xs">
            <CssBaseline />

            <Box mt={3}>
                <Link href="/" underline="none">
                    <Button variant="contained" color="primary">
                        <ArrowBackIcon /> Module Overview
                    </Button>
                </Link>
            </Box>

            <Typography variant="h2" mt={2}>
                Profile Overview
            </Typography>

            {authenticate.person && !authenticate.loading ?
                (
                    <TableContainer>
                        <Table>
                            <TableBody>
                                <TableRow>
                                    <TableCell>
                                        <Typography fontWeight="light">
                                            Firstname:
                                        </Typography>
                                    </TableCell>
                                    <TableCell>
                                        <Typography fontWeight="bold">
                                            {authenticate.person["firstname"]}
                                        </Typography>
                                    </TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell>
                                        <Typography fontWeight="light">
                                            Lastname:
                                        </Typography>
                                    </TableCell>
                                    <TableCell>
                                        <Typography fontWeight="bold">
                                            {authenticate.person["lastname"]}
                                        </Typography>
                                    </TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell>
                                        <Typography fontWeight="light">
                                            Address:
                                        </Typography>
                                    </TableCell>
                                    <TableCell>
                                        <Typography fontWeight="bold">
                                            {authenticate.person["address"]}
                                        </Typography>
                                    </TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell>
                                        <Typography fontWeight="light">
                                            Birthday:
                                        </Typography>
                                    </TableCell>
                                    <TableCell>
                                        <Typography fontWeight="bold">
                                            {authenticate.person["birthdate"]}
                                        </Typography>
                                    </TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell>
                                        <Typography fontWeight="light">
                                            Role:
                                        </Typography>
                                    </TableCell>
                                    <TableCell>
                                        <Typography fontWeight="bold">
                                            {authenticate.person["role"]}
                                        </Typography>
                                    </TableCell>
                                </TableRow>
                            </TableBody>
                        </Table>
                    </TableContainer>
                ) : (
                    <div>
                        <Skeleton width="100%" height={50} />
                        <Skeleton width="100%" height={50} />
                        <Skeleton width="100%" height={50} />
                        <Skeleton width="100%" height={50} />
                        <Skeleton width="100%" height={50} />
                    </div>
                )
            }



        </Container>
    )
}
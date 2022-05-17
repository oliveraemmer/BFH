import React from 'react'
import Card from "@mui/material/Card";
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import { useState, useEffect } from 'react';
import Api from '../../../config/Api';
import { Avatar, Divider, Grid, List, ListItem, ListItemAvatar, ListItemText, Skeleton } from '@mui/material';
import Swal from 'sweetalert2';
import { Box } from '@mui/system';
import { CalendarMonth, Description, Grade, Pages, People } from '@mui/icons-material';
import {errorMessage, successMessage} from "../../message/Message";

export const ModuleCard = ({ modulerun, user }) => {
    const [module, setModule] = useState(null);

    const [moduleLoading, setModuleLoading] = useState(true);

    const [buttonText, setButtonText] = useState(modulerun.enrolled ? "Unenroll" : "Enroll");
    const [buttonColor, setButtonColor] = useState(modulerun.enrolled ? "error" : "success");

    useEffect(() => {
        (
            async () => {
                try {
                    const { data: module } = await Api.get(`/modules/${modulerun.mid}`);
                    if (module) {
                        setModule(module);
                    }
                    setModuleLoading(false);
                } catch (error) {
                    console.log(error);
                }
            }
        )(modulerun.mid)
    }, [modulerun.mid])

    async function handleButton() {
        if (modulerun.enrolled) {
            try {
                const { status: modulerunresponse } = await Api.delete(`/enrollments/${modulerun.mrid}-${user.pid}`);
                if (modulerunresponse == 204) {
                    setButtonColor("success");
                    setButtonText("Enroll");
                    modulerun.enrolled = false;
                    successMessage.text = 'You have successfully unenrolled from this module';
                    Swal.fire(successMessage);
                } else {
                    errorMessage.text = 'An error occured while trying to unenroll (Status: ' + modulerunresponse + ')';
                    Swal.fire(errorMessage);
                }
            } catch (error) {
                errorMessage.text = 'An error occured while trying to unenroll (Status: ' + error.response.status + ')';
                Swal.fire(errorMessage);
            }
        } else {
            try {
                const { status: modulerunresponse } = await Api.post(`/enrollments/${modulerun.mrid}-${user.pid}`);
                if (modulerunresponse == 201) {
                    setButtonColor("error");
                    setButtonText("Unenroll");
                    modulerun.enrolled = true;
                    successMessage.text = 'You have successfully enrolled from this module';
                    Swal.fire(successMessage);
                } else {
                    errorMessage.text = 'An error occured while trying to enrolled (Status: ' + modulerunresponse + ')';
                    Swal.fire(errorMessage);
                }
            } catch (error) {
                errorMessage.text = 'An error occured while trying to enroll (Status: ' + error.response.status + ')';
                Swal.fire(errorMessage);
            }
        }
    }

    return (
        <Box
            marginTop={2}
        >
            {!moduleLoading ? (
                <Card>
                    <CardContent>

                        <List>
                            <ListItem>
                                <Typography variant="h5" component="div">
                                    {module.name}
                                </Typography>
                            </ListItem>
                            <ListItem>
                                <ListItemAvatar>
                                    <Avatar
                                        sx={{
                                            backgroundColor: "#90a4ae"
                                        }}
                                    >
                                        <CalendarMonth />
                                    </Avatar>
                                </ListItemAvatar>
                                <ListItemText primary={modulerun.semester + " / " + modulerun.year} secondary="Date" />
                            </ListItem>
                            <Divider variant="inset" component="li" />
                            <ListItem>
                                <ListItemAvatar>
                                    <Avatar
                                        sx={{
                                            backgroundColor: "#90a4ae"
                                        }}
                                    >
                                        <People />
                                    </Avatar>
                                </ListItemAvatar>
                                <ListItemText primary={modulerun.teachers != "" ? modulerun.teachers.map(teacher => teacher.firstname + " " + teacher.lastname).join(", ") : "*No teachers assigned*"} secondary="Teachers" />
                            </ListItem>
                            <Divider variant="inset" component="li" />
                            <ListItem>
                                <ListItemAvatar>
                                    <Avatar
                                        sx={{
                                            backgroundColor: "#90a4ae"
                                        }}
                                    >
                                        <Description />
                                    </Avatar>
                                </ListItemAvatar>
                                <ListItemText primary={modulerun.description} secondary="Description" />
                            </ListItem>
                            {modulerun.grade ? (
                                <Box>
                                    <Divider variant="inset" component="li" />
                                    <ListItem>
                                        <ListItemAvatar>
                                            <Avatar
                                                sx={{
                                                    backgroundColor: "#ffb74d"
                                                }}
                                            >
                                                <Grade />
                                            </Avatar>
                                        </ListItemAvatar>
                                        <ListItemText primary={modulerun.grade} secondary="Grade" />
                                    </ListItem>
                                </Box>
                            ) : (
                                null
                            )}
                        </List>
                        <Grid
                            container
                            direction="column"
                            justify="space-between"
                            alignItems="center"
                        >
                            <Button
                                color={buttonColor}
                                variant="outlined"
                                onClick={handleButton}
                                fullWidth
                                disabled={modulerun.grade ? true : false}
                            >
                                {buttonText}
                            </Button>
                        </Grid>
                    </CardContent>
                </Card>
            ) : (
                <Card>
                    <CardContent>
                        <Skeleton variant="rect" width={300} height={300} />
                    </CardContent>
                </Card>
            )}
        </Box>
    )

}
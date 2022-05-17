import React from 'react'
import Card from "@mui/material/Card";
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import { useState, useEffect } from 'react';
import Api from '../../../config/Api';
import { Backdrop, Dialog, DialogContent, DialogContentText, DialogTitle, List, Paper, Skeleton, ListItem, ListItemAvatar, Avatar, ListItemText, Divider, TextareaAutosize } from '@mui/material';
import Swal from 'sweetalert2';
import TextField from "@mui/material/TextField";
import FormControl from '@mui/material/FormControl';
import { Box } from '@mui/system';
import ReadMoreIcon from '@mui/icons-material/ReadMore';
import { CalendarMonth, Description, Explore, Save, ViewInAr } from '@mui/icons-material';
import {errorMessage, successMessage} from "../../message/Message";

export const ModuleDescriptionCard = ({ module, user }) => {

    const uniqueModuleId = "module-" + module.mid;

    async function handleButton(event) {
        event.preventDefault()
        try {
            const { status: modulerunresponse } = await Api.put(`/modules/${module.mid}`, {
                mid: module.mid,
                description: document.getElementById(uniqueModuleId + "-description").value,
            });
            if (modulerunresponse == 200) {
                successMessage.text = 'Module description updated';
                Swal.fire(successMessage);
            } else {
                errorMessage.text = 'An error occured while trying to update the module description (Status: ' + modulerunresponse + ')';
                Swal.fire(errorMessage);
            }
        } catch (error) {
            errorMessage.text = 'An error occured while trying to update the module description (Status: ' + error.response.status + ')';
            Swal.fire(errorMessage);
        }
    }

    return (
        <Box
            marginTop={2}
        >
            <Card variant="outlined" id={uniqueModuleId}>
                <CardContent>

                    <List>
                        <ListItem>
                            <Typography sx={{ fontSize: 14 }} color="primary" gutterBottom>
                                {module.mid}
                            </Typography>
                        </ListItem>
                        <ListItem>
                            <ListItemAvatar>
                                <Avatar
                                    sx={{
                                        backgroundColor: "#90a4ae"
                                    }}
                                >
                                    <ViewInAr />
                                </Avatar>
                            </ListItemAvatar>
                            <ListItemText>
                                <Typography variant="h5" component="div">
                                    {module.name}
                                </Typography>
                            </ListItemText>
                        </ListItem>
                        <Divider variant="inset" component="li" />
                        <ListItem>
                            <ListItemAvatar>
                                <Avatar
                                    sx={{
                                        backgroundColor: "#90a4ae"
                                    }}
                                >
                                    <Explore />
                                </Avatar>
                            </ListItemAvatar>
                            <ListItemText primary={
                                user && user.pid == module.coordinator.pid ?
                                    module.coordinator.firstname + " " + module.coordinator.lastname + " (*you)" :
                                    module.coordinator.firstname + " " + module.coordinator.lastname
                            } secondary="Coordinator" />
                        </ListItem>

                        <Box>
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

                                {user && user.pid == module.coordinator.pid ?
                                    (
                                        <FormControl fullWidth>
                                            <TextField
                                                margin="normal"
                                                required
                                                fullWidth
                                                id={uniqueModuleId + "-description"}
                                                label="Description"
                                                name="description"
                                                defaultValue={module.description}
                                                multiline
                                                rows={4}
                                            />

                                            <Button
                                                variant="contained"
                                                color="primary"
                                                onClick={handleButton}
                                                startIcon={<Save />}
                                            >
                                                Change Description
                                            </Button>
                                        </FormControl>
                                    )
                                    :

                                    <ListItemText primary={module.description} secondary="Description" />
                                }
                            </ListItem>
                        </Box>

                    </List>
                </CardContent>
            </Card>
        </Box>
    )





}
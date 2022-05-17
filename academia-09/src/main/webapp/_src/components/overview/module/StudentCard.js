import { useState, useEffect } from 'react';
import Card from "@mui/material/Card";
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import FormControl from "@mui/material/FormControl";
import Box from "@mui/material/Box";
import InputLabel from "@mui/material/InputLabel";
import Select from "@mui/material/Select";
import MenuItem from "@mui/material/MenuItem";
import Api from "../../../config/Api";
import Swal from 'sweetalert2';
import { Avatar, Divider, List, ListItem, ListItemText } from '@mui/material';
import { ListItemAvatar } from '@material-ui/core';
import { Grade } from '@mui/icons-material';
import {errorMessage, successMessage} from "../../message/Message";

export const StudentCard = ({ mrid, student }) => {
    const [grade, setGrade] = useState(student.grade ? student.grade : "");

    useEffect(() => {
        (
            async () => {
                try {
                    const { data: enrollment } = await Api.get(`/enrollments/${mrid}-${student.pid}`);
                    if (enrollment.grade) {
                        setGrade(enrollment.grade);
                    }
                } catch (error) {
                    errorMessage.text = 'An error occured while trying to load enrollments (Status: ' + error.response.status + ')';
                    Swal.fire(errorMessage);
                }
            }
        )(mrid, student.pid);
    }, [mrid, student.pid]);

    const grades =
        [
            "a",
            "b",
            "c",
            "d",
            "e",
            "f"
        ]

    const handleGrade = (event) => {
        Api.put(`/enrollments/${mrid}-${student.pid}`, { grade: event.target.value }).then(res => {
            if(res.status === 200) {
                successMessage.text = 'Grade updated';
                Swal.fire(successMessage);
            } else {
                errorMessage.text = 'Grade not updated (Status: ' + res.status + ')';
                Swal.fire(errorMessage);
            }
            setGrade(event.target.value);
        }).catch(err => {
            errorMessage.text = 'Grade not updated (Status: ' + err.response.status + ')';
            Swal.fire(errorMessage);
        })


    }

    let SelectGrade = (
        <FormControl fullWidth>
            <Box noValidate>
                <InputLabel id="gradeLabel">Grade</InputLabel>
                <Select
                    labelId="gradeLabel"
                    id="grade"
                    name="grade"
                    label="grade *"
                    value={grade}
                    onChange={handleGrade}
                    fullWidth
                >
                    {grades.map((grade) => (
                        <MenuItem key={grade} value={grade}>{grade}</MenuItem>
                    ))}
                </Select>
            </Box>
        </FormControl>
    )

    return (
        <Box
            marginTop={2}
        >
            <Card>
                <CardContent>
                    <List>
                        <ListItem>
                            <ListItemAvatar>
                                <Avatar src="/images/avatar/student.png" />
                            </ListItemAvatar>
                            <ListItemText>
                                <Typography variant="h5" component="div">
                                    {student.firstname} {student.lastname}
                                </Typography>
                            </ListItemText>
                        </ListItem>
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
                            <ListItemText>
                                {SelectGrade}
                            </ListItemText>
                        </ListItem>
                    </List>
                </CardContent>
            </Card>
        </Box>
    )
}

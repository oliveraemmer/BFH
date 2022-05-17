import { Box, Typography } from "@mui/system"
import { ModuleDescriptionCard } from "./module/ModuleDescriptionCard"
import { StudentCard } from "./module/StudentCard";
import TextField from "@mui/material/TextField";
import Select, { SelectChangeEvent } from '@mui/material/Select';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import { useState } from "react";
import { ModuleCard } from "./module/ModuleCard";
import StudentOverview from "./StudentOverview";
import Swal from "sweetalert2";
import { Container } from "@mui/material";

export default function ModuleRunOverview({ moduleruns, isTeacher, user }) {
    const [year, setYear] = useState("");
    const [semester, setSemester] = useState("");
    const [modulerunselect, setModuleRunSelect] = useState("");

    function handleSem(event) {
        event.preventDefault();
        setSemester(event.target.value);
    }

    function handleYear(event) {
        event.preventDefault();
        setYear(event.target.value);
    }

    function handleModuleRunSelect(event) {
        event.preventDefault();
        setModuleRunSelect(event.target.value);
    }

    let moduleRunsForm = (
        <Box
            sx={{
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
            }}
        >
            <FormControl fullWidth>

                <Box noValidate sx={{ mt: 1 }}>
                    <TextField
                        margin="normal"
                        required
                        fullWidth
                        id="year"
                        label="Year [yyyy]"
                        name="year"
                        defaultValue={year}
                        value={year}
                        inputProps={{
                            min: "2020",
                            max: "2030",
                            type: "number",
                            step: "1",
                            pattern: "[0-9][0-9][0-9][0-9]",
                        }}
                        color={year !== "" && year >= 2000 && year <= 2030 ? "success" : "error"}
                        error={year !== "" && year >= 2000 && year <= 2030 ? false : true}
                        onChange={handleYear}
                        autoFocus
                    />

                    <FormControl fullWidth margin="normal">
                        <InputLabel id="semesterLabel">Semester *</InputLabel>
                        <Select
                            labelId="semesterLabel"
                            id="semester"
                            name="semester"
                            label="Semester *"
                            value={semester || ""}
                            onChange={handleSem}
                            fullWidth
                        >
                            <MenuItem value="FS">FS</MenuItem>
                            <MenuItem value="HS">HS</MenuItem>
                        </Select>
                    </FormControl>

                    {/* If the user is a teacher, show the module runs input field in addition to the other form inputs */}
                    {isTeacher ?
                        (
                            <FormControl fullWidth margin="normal">
                                <InputLabel id="moduleRunLabel">Module Run*</InputLabel>
                                <Select
                                    labelId="moduleRunLabel"
                                    id="moduleRun"
                                    name="moduleRun"
                                    label="Module Run*"
                                    value={modulerunselect}
                                    onChange={handleModuleRunSelect}
                                >
                                    {moduleruns ?
                                        moduleruns.map((modulerun) => {
                                            if (modulerun.year == year && modulerun.semester == semester) {
                                                return (
                                                    <MenuItem key={modulerun.mrid}
                                                        value={modulerun.mrid || ''}>{modulerun.name}</MenuItem>
                                                )
                                            } else {
                                                return null
                                            }
                                        })
                                        :
                                        <MenuItem value="">Loading...</MenuItem>
                                    }
                                </Select>
                            </FormControl>
                        ) :
                        (
                            null
                        )
                    }

                </Box>
            </FormControl>
        </Box>
    )

    return (
        <Container>
            {moduleRunsForm}
            {
                isTeacher && moduleruns ?
                    year && semester && modulerunselect ?
                        moduleruns.map(modulerun => (
                            modulerunselect == modulerun.mrid ?
                                (
                                    <StudentOverview
                                        key={modulerun.mrid}
                                        mrid={modulerun.mrid}
                                        students={modulerun.enrollments}
                                    />
                                )
                                :
                                null
                        ))
                        :
                        null
                    :
                    moduleruns.map(modulerun => (
                        year == modulerun.year && semester == modulerun.semester ?
                            <ModuleCard key={module.mrid} modulerun={modulerun} user={user} />
                            :
                            null
                    ))
            }
        </Container>
    )

}
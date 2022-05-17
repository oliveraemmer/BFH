import Typography from "@mui/material/Typography";
import * as React from "react";
import Container from '@mui/material/Container';
import CssBaseline from "@mui/material/CssBaseline";
import Box from '@mui/material/Box';
import ViewModuleIcon from '@mui/icons-material/ViewModule';
import ViewTimelineIcon from '@mui/icons-material/ViewTimeline';
import { useAuth } from "../contexts/Auth";
import Api from "../config/Api";
import { useEffect, useState } from "react";
import { Skeleton } from "@mui/material";
import StudentOverview from "../components/overview/StudentOverview";
import Button from "@mui/material/Button";
import ModuleOverview from "../components/overview/ModuleOverview";
import { BottomNavigation, BottomNavigationAction } from "@mui/material";
import ModuleRunOverview from "../components/overview/ModuleRunOverview";
import AccountBoxIcon from '@mui/icons-material/AccountBox';
import {errorMessage} from "../components/message/Message";
import Swal from "sweetalert2";

export default function Overview() {
    const authenticate = useAuth();
    const [loadingModuleruns, setLoadingModuleruns] = useState(true);
    const [loadingModules, setLoadingModules] = useState(true);

    const [moduleruns, setModuleruns] = useState(null);
    const [modules, setModules] = useState(null);
    const [currentView, setCurrentView] = useState("moduleruns");

    let moduleViewNavigation = (
        <Box
            paddingTop={2}
        >
            <BottomNavigation
                showLabels
                value={currentView}
                onChange={(event, newValue) => {
                    setCurrentView(newValue);
                }}
            >
                <BottomNavigationAction value="moduleruns" label="Moduleruns" icon={<ViewTimelineIcon />} />
                {authenticate.user && authenticate.user["role"] == "teacher" ?
                    (
                        <BottomNavigationAction value="modules" label="Modules" icon={<ViewModuleIcon />} />
                    )
                    : null
                }
            </BottomNavigation>
        </Box>
    )

    // init data
    let isTeacher = false
    let loggedInUser = ""

    if (authenticate.user) {
        if (authenticate.user["role"] === "teacher") {
            isTeacher = true
        }
        loggedInUser = authenticate.user
    }

    useEffect(() => {
        (
            async () => {
                try {
                    const { data: response } = await Api.get("/moduleruns");
                    setModuleruns(response);
                    setLoadingModuleruns(false);
                } catch (error) {
                    errorMessage.text = 'An error occured while trying to load moduleruns (Status: ' + error.response.status + ')';
                    Swal.fire(errorMessage);
                }
            }
        )(),
            (
                async () => {
                    try {
                        const { data: response } = await Api.get("/modules");
                        setModules(response);
                        setLoadingModules(false);
                    } catch (error) {
                        errorMessage.text = 'An error occured while trying to load modules (Status: ' + error.response.status + ')';
                        Swal.fire(errorMessage);
                    }
                }
            )()
    }, [currentView]);

    return (
        <Container component="main" maxWidth="xs">
            <CssBaseline />
            <Typography component="h1" variant="h3" mt={2}>
                {!authenticate.loading ?
                    isTeacher ? "Teacher Overview" : "Student Overview"
                    :
                    <Skeleton />}
            </Typography>

            {moduleViewNavigation}

            {currentView === "moduleruns" ?

                !loadingModuleruns && !authenticate.loading ?
                    <ModuleRunOverview moduleruns={moduleruns} isTeacher={isTeacher} user={authenticate.user} />
                    :
                    <Skeleton />
                :
                !loadingModules && !authenticate.loading ?
                    <ModuleOverview modules={modules} isTeacher={isTeacher} user={authenticate.user} />
                    :
                    <Skeleton />
            }
        </Container>

    )

}

import { Skeleton } from "@mui/material"
import { StudentCard } from "./module/StudentCard"

export default function StudentOverview({ mrid, students }) {
    return (
        <div>
            {students.map(student => (
                <StudentCard key={student.pid} student={student} mrid={mrid} />
            ))}
        </div>
    )
}
import { Box, Typography } from "@mui/system"
import { ModuleDescriptionCard } from "./module/ModuleDescriptionCard"

export default function ModuleOverview({ modules, user }) {

    modules.sort((a, b) => {
        if (a.mid < b.mid) {
            return -1;
        }
        if (a.mid > b.mid) {
            return 1;
        }
        return 0;
    })

    return (
        <div>
            {modules.map(module => (
                <ModuleDescriptionCard key={module.mid} module={module} user={user} />
            ))}
        </div>
    )

}
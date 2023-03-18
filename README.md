# Velocity Server Switch

Minecraft "Velocity" Proxy's includes a command to allow the player to switch between servers `/server <servername>`,

but the command does not actually exist on the servers themselves. This causes issues when you'd like to run the command through another source like a normal ingame command, via another mod, MCF or a command block.



This mod adds that command to the fabric server and can be used like any other command, but emulates the functionally of the Velocity Proxy server switch 

command: `/velocityserverswitch <servername>`


## Permissions

permissions node to enable use: `velocityserverswitch.command`

and to enable a server `veloccityserverswitch.<server>`
for eg `veloccityserverswitch.survival`

if they dont have permission to enter, the user will get a message telling them so.

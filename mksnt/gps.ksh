# gps - a graphical ps/kill utility

# Enter the name of your debugger (plus command line options) here
# to be run when 'Debug' is pressed.
# The default is Visual C++ 4.
DEBUGGER="msdev -p"

# Dialog resources are included at the end of this file
RES=$(whence $0)

# Here are some constants for the controls in the dialog.
IDOK=1
IDCANCEL=2
IDC_KILL=100
IDC_REFRESH=101
IDC_LIST=102
IDC_PERIOD=103
IDC_SECONDS=104
IDC_THREADS=105
IDC_DEBUG=106

# Make these variables into integers to make math faster
typeset -i first win95 width height dlgwidth dlgheight xpos ypos

# This function fills up the list box with the output from the ps command.
function refresh_list
{
	# Indicate busy!
	dlg cursor -p h -a HOURGLASS

	# Clear out the list box
	dlg clear -c $IDC_LIST

	first=0

	# Read in the output from 'ps' using a here document.
	# 'ps' produces different output on Win95 vs NT, so we need
	# to read it differently.
	if [ win95 -eq 1 ]
	then
		while IFS=" " read -r pid ppid cnt thr pri mod heap stat name
		do
			if [ first -eq 0 ]
			then
				# Skip the ps header
				first=1
				continue
			fi

			dlg addtext -c $IDC_LIST \
				"$name" "$pid" "$ppid" "$stat" "$cnt" \
				"$thr" "$pri" "$mod" "$heap"

		done <<EOF
$(ps -l)
EOF
	else
		while IFS=" " read -r pid pri size rss start time command name
		do
			if [ first -eq 0 ]
			then
				# Skip the ps header
				first=1
				continue
			fi

			dlg addtext -c $IDC_LIST \
				"$command" "$pid" "$start" \
				"$time" "$pri" "$size" "$rss"

	done <<EOF
$(ps -Al)
EOF
	fi
	dlg cursor -h $h
}

#
# Display a sub-dialog containing the list of the threads for the selected
# process passed into this function as $1
#
function threadinfo
{
	if dlg load -m $maindlgh -x -1 -y -1 $RES THREAD_DIALOG
	then
		dlg layout "move right($IDOK), right(parent) -6" \
			"stretch right($IDC_LIST), left($IDOK), -6" \
			"stretch bottom($IDC_LIST), bottom(parent), -6"
		dlg cursor -p h -a HOURGLASS
		dlg columns -c $IDC_LIST ThreadID 70 Cnt 35 BasePri 50 DeltaPri 50 Flags 80
		dlg settext "Threads for Process "$1

		# Output should be header, process line, thread-header,
		# list of thread info lines.
		#    ThreadID  Cnt BasePri DeltaPri Flags
		#   0xFFCF223B 0   13      0        0x00000000
		i=0
		while IFS=" " read -r tid cnt base delta flags
		do
			if [ $i -gt 2 ]
			then
				dlg addtext -c $IDC_LIST "$tid" "$cnt" "$base" "$delta" "$flags"
			fi
			let i=i+1
		done <<EOF
$(ps -t $1)
EOF
		dlg cursor -h $h
		if [ $i -lt 3 ]
		then
			dlg settext "Process $1 has exited"
		fi
		while dlg event m c; do
			case "$m $c" in
			"command $IDOK")
				break
				;;
			esac
		done
		dlg close
	fi
}


# This is where the program really begins.  Load the dialog.
if ! dlg load -r maindlgh -x -1 -y -1 $RES GPS_DIALOG
then
	exit
fi

dlg layout "move right($IDOK), right(parent) -6" \
	   "move top($IDC_KILL), bottom(1), +6" \
	   "move top($IDC_REFRESH), bottom($IDC_KILL), +6" \
	   "move top($IDC_PERIOD), bottom($IDC_REFRESH), +6" \
	   "move top($IDC_THREADS), bottom($IDC_PERIOD), +6" \
	   "move top($IDC_DEBUG), bottom($IDC_THREADS), +6" \
	   "move left($IDOK), right($IDC_LIST), +6" \
	   "move left($IDC_KILL), left($IDOK)" \
	   "move left($IDC_REFRESH), left($IDOK)" \
	   "move left($IDC_PERIOD), left($IDOK)" \
	   "move left($IDC_THREADS), left($IDOK)" \
	   "move left($IDC_DEBUG), left($IDOK)" \
	   "stretch right($IDC_LIST), left($IDOK), -6" \
	   "stretch bottom($IDC_LIST), bottom(parent), -6"

# ps provides different output on Win95 vs NT, so we need to distinguish
# between the two.
if [ $(uname) = Windows_95 ]
then
	win95=1
	dlg columns -c $IDC_LIST Command 110 "Process ID" 80 "Parent ID" 80 Flags 60 Count 45 Thread 48 Priority 60 "Module ID" 85 "Heap ID" 85
else
	win95=0
	dlg columns -c $IDC_LIST Command 100 ID 50 Start 75 Time 50 Priority 50 Size 50 Rss 50
	# We don't currently have a method of enumerating threads on NT
	dlg enabled -c $IDC_THREADS 0
fi

# Intially, fill the dialog with the first ps output
refresh_list
tout=	# No autorefresh by default
sec=

# The event loop for the dialog
while : ; do
	dlg event $tout msg control
	case $msg in
	close)
		break
		;;
	timeout)
		refresh_list
		;;
	command)
		case $control in
		$IDOK)
			# We're done.
			break ;;
		$IDC_REFRESH)
			# Refresh the process list.
			refresh_list ;;
		$IDC_KILL)
			# Kill the selected process.
			dlg getcursel -c $IDC_LIST index
			if [ $index -ne -1 ]
			then
				dlg gettext -c $IDC_LIST -i $index command pid
				err=$(kill $pid 2>&1)
				if [ $? -ne 0 ]
				then
					msgbox "Error Killing "$pid "$err"
				fi
				# Update the process list.
				refresh_list
			else
				msgbox -i exclamation "Process Status" "You must select a process to kill."
			fi ;;
		$IDC_DEBUG)
			# Debug the selected process
			dlg getcursel -c $IDC_LIST index
			if [ $index -ne -1 ]
			then
				dlg gettext -c $IDC_LIST -i $index command pid
				start $DEBUGGER $pid
			else
				msgbox -i exclamation "Process Status" "You must select a process to debug."
			fi ;;
		$IDC_PERIOD)
			# Set auto-refresh rate
			if dlg load -m $maindlgh -x -1 -y -1 $RES REFRESH_DIALOG
			then
				dlg settext -c $IDC_SECONDS "$sec"
				dlg setfocus -c $IDC_SECONDS
				while dlg event m c; do
					case "$m $c" in
					"command $IDOK")
						dlg gettext -c $IDC_SECONDS s
						if [ -z "$s" ]
						then
							tout=
							sec=
						else
							tout="-t${s}000"
							sec=$s
						fi
						break
						;;
					"command $IDCANCEL")
						break
						;;
					esac
				done
				dlg close
			fi
			;;
		$IDC_THREADS)
			# More information on current process
			dlg getcursel -c $IDC_LIST index
			if [ $index -ne -1 ]
			then
				dlg gettext -c $IDC_LIST -i $index command pid
				threadinfo $pid
			fi
		esac
		;;

	esac
done

dlg close

# That's all folks.

# Dialog resources follow this line
#[]mks internal resource : dialog : GPS_DIALOG
#[ ]begin : size 426
#[  ]M0 #.D      '  8 #P ] :0      %  <@!O &, 90!S ', ( !3 '0 80!T
#[  ]M '4 <P    @ 30!3 "  4P!A &X <P @ %, 90!R &D 9@         !4   
#[  ]M   ! 0< ,@ .  $ __^  $, ; !O ', 90         !4      ! 1D ,@ .
#[  ]M &0 __^  "8 2P!I &P ;  @ %  <@!O &, 90!S ',          5      
#[  ]M 0$K #( #@!E /__@  F %( 90!F '( 90!S &@         !0"!4      )
#[  ]M  < Y@"4 &8 4P!Y ', 3 !I ', = !6 &D 90!W #, ,@   $< 90!N &4 
#[  ]M<@!I &, ,0            %0      $!/0 R  X 9P#__X  )@!0 &4 <@!I
#[  ]M &\ 9 !I &, +@ N "X            !4      ! 4\ ,@ . &D __^  "8 
#[  ]M5 !H '( 90!A &0 <P N "X +@         !4      ! 6$ ,@ . &H __^ 
#[  ]5 $0 90!B '4 9P N "X +@      
#[ ]end
#[]mks internal resource : dialog : REFRESH_DIALOG
#[ ]begin : size 482
#[  ]MP #(@      $      "Z $4      %  90!R &D ;P!D &D 8P @ %  <@!O
#[  ]M &, 90!S ', ( !2 &4 9@!R &4 <P!H    " !- %, ( !3 &$ ;@!S "  
#[  ]M4P!E '( :0!F       !  %0     ($ !P R  X  0#__X  3P!+        
#[  ]M     5      @0 8 #( #@ " /__@ !# &$ ;@!C &4 ;             )0
#[  ]M      < !P!I "0 _____X( 10!N '0 90!R "  = !H &4 ( !N '4 ;0!B
#[  ]M &4 <@ @ &\ 9@ @ ', 90!C &\ ;@!D ', ( !T &\ ( !E &P 80!P ', 
#[  ]M90 @ &( 90!T '< 90!E &X ( !A '4 = !O &T 80!T &D 8P!A &P ; !Y
#[  ]M "  <@!E &8 <@!E ', : !I &X 9P @ '0 : !E "  < !R &\ 8P!E ', 
#[  ]M<P @ &P :0!S '0 +@ @ "  0P!L &4 80!R "  = !O "  = !U '( ;@ @
#[  ]M &\ 9@!F "  80!U '0 ;P!R &T 80!T &D 8P @ '( 90!F '( 90!S &@ 
#[  ]@+@      @ "!4      ' #$ ;  , &@ __^!        
#[ ]end
#[]mks internal resource : dialog : THREAD_DIALOG
#[ ]begin : size 222
#[  ]M0 #,@      "       * 6X      %0 : !R &4 80!D ', ( !F &\ <@ @
#[  ]M %  <@!O &, 90!S ', (  P '@ 6 !8 %@ 6 !8 %@ 6 !8    " !- %, 
#[  ]M( !3 &$ ;@!S "  4P!E '( :0!F       !  %0     -$ !P R  X  0#_
#[  ]M_X  )@!# &P ;P!S &4         !0"!4      '  < P0!6 &8 4P!Y ', 
#[  ]J3 !I ', = !6 &D 90!W #, ,@   $< 90!N &4 <@!I &, ,0      
#[ ]end
#[]mks internal resource : icon : \0 : width 32 height 32 colors 16
#[ ]begin : size 744
#[  ]M*    "    !      0 $      "  @                              
#[  ]M (   (    " @ "     @ "  ("   # P,  @("     _P  _P   /__ /\ 
#[  ]M  #_ /\ __\  /___P     (B(B(B(B(B(B(B(B(                    
#[  ]M"(   #,S,S,S,S,S,S,S,PB(  "[N[N[N[N[NP   /,("(  O[^_O[^_O[^_
#[  ]MO[^S"!B(                   !"(              B(B $!"(        
#[  ]M        @!$!"(,S,S,S,S,S,S,S,( 1$!#_N[N[N[N[N[N[NS" $1$ _[N[
#[  ]MN[N[N[N[N[LP@/$1"P______\     "[,("?$+NP     /_\_/SPNS" &0_[
#[  ]M        _\_/P+LP@ "_\ #_____  _\_/"[,(  "P /     /  _\_ NS" 
#[  ]M   /\ J@"@ /  _\\+LP@   #_"@"@H* / /_\"[,(    _PH H*"@ / /SP
#[  ]MNS"    /\ J@"@"J#P#_P+LP@   #_        \ _/"[,(    _P"@"J * /
#[  ]M /  NS"    /\ H* *"@#P"[N[LP@   #_ *"@"@H \ ^[N[,(    _P"@"J
#[  ]M * / /___S"    /\       \ #P        #_________            #_
#[  ]M______\                                                     
#[  ]M                                         /X   #X    >    #@ 
#[  ]M   8    "    0?   $    !     0    $    !     0    $    !    
#[  ]M <    '@   !X    <    '    !P    <    '    !P    <    '    #
#[  ]8X  '_^  !__@  __^  ?__P /___ /__
#[ ]end
#[]mks internal resource : icon : \1 : width 16 height 16 colors 16
#[ ]begin : size 296
#[  ]M*    !     @     0 $      #                                 
#[  ]M (   (    " @ "     @ "  ("   # P,  @("     _P  _P   /__ /\ 
#[  ]M  #_ /\ __\  /___P           ( +N[N[N[NP"          0@ ,S,S,S
#[  ]M !$(       ($0@S,[N[NPB0L   #\S+")L J@H _,L( /"@"J /RP@ \*H*
#[  ]M"@_+" #P    #\L( / *"@H/NP@ \*H*J@\   #P          ____      
#[  ]M         ,  F 1          !@#      ,                 @    (  
#[  ]:  "     @    ( !  " 'P8 P#\  .!_  "?
#[ ]end

# Auto-generated by EclipseNSIS Script Wizard
# 08.12.2017 13:14:42

!include LogicLib.nsh

Name "AutomationStudio"

# General Symbol Definitions
!define REGKEY "SOFTWARE\$(^Name)"
!define VERSION "2.0.0.1"
!define COMPANY "Bichler Technologies GmbH"
!define URL "www.bichler.tech"
!define ARP "Software\Microsoft\Windows\CurrentVersion\Uninstall\BTech\AutomationStudio"


# MultiUser Symbol Definitions
!define MULTIUSER_EXECUTIONLEVEL Highest
!define MULTIUSER_INSTALLMODE_DEFAULT_CURRENTUSER
!define MULTIUSER_MUI
!define MULTIUSER_INSTALLMODE_DEFAULT_REGISTRY_KEY "${REGKEY}"
!define MULTIUSER_INSTALLMODE_DEFAULT_REGISTRY_VALUENAME MultiUserInstallMode
!define MULTIUSER_INSTALLMODE_COMMANDLINE
!define MULTIUSER_INSTALLMODE_INSTDIR "BTech\AutomationStudio"
!define MULTIUSER_INSTALLMODE_INSTDIR_REGISTRY_KEY "${REGKEY}"
!define MULTIUSER_INSTALLMODE_INSTDIR_REGISTRY_VALUE "Path"


# MUI Symbol Definitions
!define MUI_ICON ".\icons\splash.ico"
!define MUI_FINISHPAGE_NOAUTOCLOSE
!define MUI_LICENSEPAGE_RADIOBUTTONS
!define MUI_STARTMENUPAGE_REGISTRY_ROOT HKLM
!define MUI_STARTMENUPAGE_REGISTRY_KEY ${REGKEY}
!define MUI_STARTMENUPAGE_REGISTRY_VALUENAME StartMenuGroup
!define MUI_STARTMENUPAGE_DEFAULTFOLDER AutomationStudio
!define MUI_FINISHPAGE_RUN "$INSTDIR\AutomationStudio.exe"
!define MUI_FINISHPAGE_SHOWREADME ""
!define MUI_FINISHPAGE_SHOWREADME_NOTCHECKED
!define MUI_FINISHPAGE_SHOWREADME_TEXT "Verkn�pfung am Desktop erstellen"
!define MUI_FINISHPAGE_SHOWREADME_FUNCTION finishpageaction
!define MUI_UNICON ".\icons\splash.ico"
!define MUI_UNFINISHPAGE_NOAUTOCLOSE

# Included files
!include MultiUser.nsh
!include Sections.nsh
!include MUI2.nsh
!include FileFunc.nsh

# welcome page images
!define MUI_HEADERIMAGE_RIGHT
!define MUI_WELCOMEFINISHPAGE_BITMAP ".\icons\welcome_splash.bmp"
!define MUI_ABORTWARNING

# Variables
Var StartMenuGroup

;--------------------------------
;Interface Settings

  ;!define MUI_ABORTWARNING

  ;Show all languages, despite user's codepage
  !define MUI_LANGDLL_ALLLANGUAGES

;--------------------------------
;Language Selection Dialog Settings

  ;Remember the installer language
  ;!define MUI_LANGDLL_REGISTRY_ROOT "HKCU" 
  ;!define MUI_LANGDLL_REGISTRY_KEY "Software\Automation Studio" 
  ;!define MUI_LANGDLL_REGISTRY_VALUENAME "Installer Language"

;--------------------------------

; Installer pages
!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_LICENSE license.txt
!insertmacro MULTIUSER_PAGE_INSTALLMODE
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_STARTMENU Application $StartMenuGroup
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH
!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES

# Installer languages
!insertmacro MUI_LANGUAGE "English" ; The first language is the default language
!insertmacro MUI_LANGUAGE "German"


# Installer attributes
OutFile "setup\AutomationStudio.exe"
InstallDir AutomationStudio
CRCCheck on
XPStyle off
ShowInstDetails show
VIProductVersion 2.0.0.0
VIAddVersionKey ProductName "AutomationStudio"
VIAddVersionKey ProductVersion "${VERSION}"
VIAddVersionKey CompanyName "${COMPANY}"
VIAddVersionKey CompanyWebsite "${URL}"
VIAddVersionKey FileVersion "${VERSION}"
VIAddVersionKey FileDescription ""
VIAddVersionKey LegalCopyright ""
InstallDirRegKey HKLM "${REGKEY}" Path
ShowUninstDetails show

Function finishpageaction
CreateShortcut "$desktop\$(^Name).lnk" "$instdir\$(^Name).exe"
FunctionEnd


# Installer sections
Section -Main SEC0000
    SetOutPath $INSTDIR
    SetOverwrite on
	File /r "/home/runner/work/BichlerTechStudio/BichlerTechStudio/releng/com.bichler.automationstudio.product/target/products/com.bichler.astudio.product.feature/win32/win32/x86_64/*"
   # File /r "C:\Users\Thomas\Documents\java\product\AutomationStudio\*"
   # File C:\AutomationStudio\AutomationStudio.exe
    WriteRegStr HKLM "${REGKEY}\Components" Main 1
SectionEnd

Section -post SEC0001
    WriteRegStr HKLM "${REGKEY}" Path $INSTDIR
    SetOutPath $INSTDIR
    WriteUninstaller $INSTDIR\uninstall.exe
    !insertmacro MUI_STARTMENU_WRITE_BEGIN Application
    SetOutPath $SMPROGRAMS\$StartMenuGroup
    CreateShortcut "$SMPROGRAMS\$StartMenuGroup\Uninstall $(^Name).lnk" $INSTDIR\uninstall.exe
    CreateShortcut "$SMPROGRAMS\$StartMenuGroup\$(^Name).lnk" $INSTDIR\$(^Name).exe
    SetOutPath $INSTDIR
    CreateShortCut "$SMSTARTUP\SysTray.lnk" $INSTDIR\SysTray.jar
    !insertmacro MUI_STARTMENU_WRITE_END
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayName "$(^Name)"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayVersion "${VERSION}"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" Publisher "${COMPANY}"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" URLInfoAbout "${URL}"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayIcon $INSTDIR\uninstall.exe
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" UninstallString $INSTDIR\uninstall.exe
    WriteRegDWORD HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" NoModify 1
    WriteRegDWORD HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" NoRepair 1
     ${GetSize} "$INSTDIR" "/S=0K" $0 $1 $2
    IntFmt $0 "0x%08X" $0
    WriteRegDWORD HKLM "${ARP}" "EstimatedSize" "$0"
SectionEnd

# Macro for selecting uninstaller sections
!macro SELECT_UNSECTION SECTION_NAME UNSECTION_ID
    Push $R0
    ReadRegStr $R0 HKLM "${REGKEY}\Components" "${SECTION_NAME}"
    StrCmp $R0 1 0 next${UNSECTION_ID}
    !insertmacro SelectSection "${UNSECTION_ID}"
    GoTo done${UNSECTION_ID}
next${UNSECTION_ID}:
    !insertmacro UnselectSection "${UNSECTION_ID}"
done${UNSECTION_ID}:
    Pop $R0
!macroend

!macro UninstallExisting exitcode uninstcommand
	Push `${uninstcommand}`
	Call UninstallExisting
	Pop ${exitcode}
!macroend

Function UninstallExisting
	Exch $1 ; uninstcommand
	Push $2 ; Uninstaller
	Push $3 ; Len
	StrCpy $3 ""
	StrCpy $2 $1 1
	StrCmp $2 '"' qloop sloop
	sloop:
		StrCpy $2 $1 1 $3
		IntOp $3 $3 + 1
		StrCmp $2 "" +2
		StrCmp $2 ' ' 0 sloop
		IntOp $3 $3 - 1
		Goto run
	qloop:
		StrCmp $3 "" 0 +2
		StrCpy $1 $1 "" 1 ; Remove initial quote
		IntOp $3 $3 + 1
		StrCpy $2 $1 1 $3
		StrCmp $2 "" +2
		StrCmp $2 '"' 0 qloop
	run:
		StrCpy $2 $1 $3 ; Path to uninstaller
		StrCpy $1 161 ; ERROR_BAD_PATHNAME
		GetFullPathName $3 "$2\.." ; $InstDir
		IfFileExists "$2" 0 +4
		ExecWait '"$2" /S _?=$3' $1 ; This assumes the existing uninstaller is a NSIS uninstaller, other uninstallers don't support /S nor _?=
		IntCmp $1 0 "" +2 +2 ; Don't delete the installer if it was aborted
		Delete "$2" ; Delete the uninstaller
		RMDir "$3" ; Try to delete $InstDir
		RMDir "$3\.." ; (Optional) Try to delete the parent of $InstDir
	Pop $3
	Pop $2
	Exch $1 ; exitcode
FunctionEnd

# Uninstaller sections
Section /o -un.Main UNSEC0000
    Delete $INSTDIR\AutomationStudio.exe
    RmDir /r $INSTDIR
    DeleteRegValue HKLM "${REGKEY}\Components" Main
SectionEnd

Section -un.post UNSEC0001
    DeleteRegKey HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)"
    Delete "$DESKTOP\$(^Name).lnk"
    Delete "$SMPROGRAMS\$StartMenuGroup\Uninstall $(^Name).lnk"
    Delete $INSTDIR\uninstall.exe
    Delete "$SMSTARTUP\SysTray.lnk"
    DeleteRegValue HKLM "${REGKEY}" StartMenuGroup
    DeleteRegValue HKLM "${REGKEY}" Path
    DeleteRegKey /IfEmpty HKLM "${REGKEY}\Components"
    DeleteRegKey /IfEmpty HKLM "${REGKEY}"
    RmDir /r $SMPROGRAMS\$StartMenuGroup
    RmDir /r $INSTDIR
    RMDir $INSTDIR
    Push $R0
    StrCpy $R0 $StartMenuGroup 1
    StrCmp $R0 ">" no_smgroup
no_smgroup:
    Pop $R0
SectionEnd

# Installer functions
Function .onInit
	ReadRegStr $0 HKCU "Software\Software\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" "UninstallString"
	${If} $0 != ""
	${AndIf} ${Cmd} `MessageBox MB_YESNO|MB_ICONQUESTION "Uninstall previous version?" /SD IDYES IDYES`
		!insertmacro UninstallExisting $0 $0
		${If} $0 <> 0
			MessageBox MB_YESNO|MB_ICONSTOP "Failed to uninstall, continue anyway?" /SD IDYES IDYES +2
				Abort
		${EndIf}
	${EndIf}
    InitPluginsDir
    !insertmacro MUI_LANGDLL_DISPLAY
    !insertmacro MULTIUSER_INIT
    ;write selected language to init file
    FileOpen $4 "$INSTDIR\Automation Studio.ini" a
	FileSeek $4 0 END
	;FileWrite $4 "$\r$\n" ; we write a new line
	FileWrite $4 "-nl" ;
	FileWrite $4 "$\r$\n" ; we write a new line
	${If} $LANGUAGE == "1031"
		FileWrite $4 "en"
	${Else}
		FileWrite $4 "de"
	${EndIf}
	FileWrite $4 "$\r$\n" ; we write a new line
	FileClose $4 ; and close the file
FunctionEnd

# Uninstaller functions
Function un.onInit
    !insertmacro MUI_STARTMENU_GETFOLDER Application $StartMenuGroup
    !insertmacro MULTIUSER_UNINIT
    !insertmacro SELECT_UNSECTION Main ${UNSEC0000}
FunctionEnd


LOCAL_PATH :=	$(call my-dir) 

include  $(CLEAR_VARS)

LOCAL_CPPFLAGS :=--std=c++11
LOCAL_MODULE    := OCT
LOCAL_SRC_FILES := com_calculate_OCT.cpp ThirdOctave.cpp Tools.cpp math_ex.cpp Level.cpp AddWeighting.cpp
LOCAL_LDLIBS := -landroid                        
include  $(BUILD_SHARED_LIBRARY)

include  $(CLEAR_VARS)

LOCAL_CPPFLAGS :=--std=c++11
LOCAL_MODULE    := AI
LOCAL_SRC_FILES := com_calculate_Math.cpp ArticulationIndex.cpp ThirdOctave.cpp Tools.cpp math_ex.cpp Level.cpp
LOCAL_LDLIBS := -landroid
LOCAL_LDLIBS += -L$(SYSROOT)/usr/lib -llog                 
include  $(BUILD_SHARED_LIBRARY)

include   $(CLEAR_VARS)

LOCAL_CPPFLAGS :=--std=c++11     
LOCAL_MODULE    := FFT
LOCAL_SRC_FILES := com_calculate_FFT.cpp fft.cpp fft_tools.cpp palette.cpp math_ex.cpp AddWeighting.cpp Level.cpp Tools.cpp
LOCAL_LDLIBS := -landroid                        
include  $(BUILD_SHARED_LIBRARY)

include  $(CLEAR_VARS)
LOCAL_CPPFLAGS :=--std=c++11
LOCAL_MODULE    := SPL
LOCAL_SRC_FILES := com_calculate_SPL.cpp Level.cpp Tools.cpp math_ex.cpp
LOCAL_LDLIBS := -landroid                        
include  $(BUILD_SHARED_LIBRARY)

include	 $(CLEAR_VARS)
LOCAL_CPPFLAGS :=--std=c++11     
LOCAL_MODULE    := RPM
LOCAL_SRC_FILES := com_calculate_RPM.cpp rpm.cpp
LOCAL_LDLIBS := -landroid                        
include 	$(BUILD_SHARED_LIBRARY)

include	 $(CLEAR_VARS)
LOCAL_CPPFLAGS :=--std=c++11   -pthread -frtti 
LOCAL_MODULE    := Order
LOCAL_SRC_FILES := com_calculate_Order.cpp OrderAnalysis.cpp rpm.cpp fft.cpp math_ex.cpp fft_tools.cpp 
LOCAL_LDLIBS := -landroid                        
include 	$(BUILD_SHARED_LIBRARY)

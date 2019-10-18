package com.sensorberg.permissionbitte.sample;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ArchitectureComponentsViewModel extends ViewModel {

	private final MutableLiveData<State> state;

	public ArchitectureComponentsViewModel() {
		state = new MutableLiveData<>();
	}

	LiveData<State> getState() {
		return state;
	}

	void shouldAskPermission(boolean shouldAsk) {
		state.setValue(shouldAsk ? State.SHOW_PERMISSION_BUTTON : State.PERMISSION_GOOD);
	}

	void userAgreesForPermissionAsking() {
		state.setValue(State.ASK_FOR_PERMISSION);
	}

	public void userDeclinedRationale() {
		state.setValue(State.ON_PERMISSION_RATIONALE_DECLINED);
	}

	enum State {
		SHOW_PERMISSION_BUTTON,
		ASK_FOR_PERMISSION,
		SHOW_PERMISSION_RATIONALE,
		ON_PERMISSION_DENIED,
		ON_PERMISSION_RATIONALE_DECLINED,
		PERMISSION_GOOD,
	}
}

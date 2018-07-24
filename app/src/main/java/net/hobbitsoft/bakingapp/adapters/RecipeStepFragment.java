package net.hobbitsoft.bakingapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import net.hobbitsoft.bakingapp.R;
import net.hobbitsoft.bakingapp.recipes.Step;

/**
 * A simple {@link Fragment} subclass.
 * to handle interaction events.
 * Use the {@link RecipeStepFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeStepFragment extends Fragment {

    private static final String TAG = RecipeStepFragment.class.getSimpleName();
    private static final String STEP = "step";
    private static final String POSTION = "position";

    private static Step mStep;

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mExoPlayerView;
    private long mPosition;
    private TextView description;

    public RecipeStepFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param Step mStep;
     * @return A new instance of fragment RecipeStepFragment.
     */

    public static RecipeStepFragment newInstance(Step step) {
        mStep = step;

        RecipeStepFragment fragment = new RecipeStepFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(STEP, step);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setStep(Step step) {
        mStep = step;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mStep = (Step) getArguments().getSerializable(STEP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View stepView = inflater.inflate(R.layout.fragment_recipe_step, container, false);

        if (savedInstanceState != null) {
            mPosition = savedInstanceState.getLong(POSTION, C.POSITION_UNSET);
        } else {
            mPosition = C.POSITION_UNSET;
        }

        mExoPlayerView = stepView.findViewById(R.id.step_video_player);

        if (mStep.getVideoURL().isEmpty() || mStep.getVideoURL() == null) {
            mExoPlayerView.setCustomErrorMessage("No Video Associated with thie Instruction");
        } else {
            createPlayer();
        }

        description = stepView.findViewById(R.id.step_description);

        if (description != null) description.setText(mStep.getDescription());

        return stepView;

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong(POSTION, mPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onStop() {
        super.onStop();
        destroyPlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyPlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) mPosition = mExoPlayer.getCurrentPosition();
        destroyPlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (description != null) description.setText(mStep.getDescription());
        if (mExoPlayer != null && mPosition != C.POSITION_UNSET) mExoPlayer.seekTo(mPosition);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        destroyPlayer();
    }

    private void createPlayer() {
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
        mExoPlayerView.setPlayer(mExoPlayer);
        Uri videoUri = Uri.parse(mStep.getVideoURL()).buildUpon().build();
        String userAgent = Util.getUserAgent(getContext(), "BakingApp");
        MediaSource mediaSource = new ExtractorMediaSource(videoUri,
                new DefaultDataSourceFactory(getContext(), userAgent),
                new DefaultExtractorsFactory(), null, null);
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(true);
    }

    private void destroyPlayer() {

        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
}

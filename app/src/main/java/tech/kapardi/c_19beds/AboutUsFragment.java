package tech.kapardi.c_19beds;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutUsFragment extends BottomSheetDialogFragment {

    public AboutUsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        String versionName = BuildConfig.VERSION_NAME;
        String description = "Kapardi Studio - Innovative Ideas Interpreted ";
        Element versionElement = new Element();
        versionElement.setTitle("Version "+versionName);

        View aboutPage = new AboutPage(getActivity())
                .isRTL(false)
                .enableDarkMode(false)
                .setDescription(description)
                //.setCustomFont(R.and) // or Typeface
                .setImage(R.drawable.c19_beds)
                .addItem(versionElement)
                .addGroup("Connect with us")
                .addEmail("kapardi.labs@gmail.com")
                .addWebsite("https://google.com/")
                .addFacebook("kapardistudio")
                .addTwitter("kapardi_studio")
                .addYoutube("UC-4Srmid8TNAqMjdZ1QQI7w")
                .addPlayStore("tech.kapardi.c_19beds")
                //.addGitHub("medyo")
                .addInstagram("kapardi_studio")
                .create();

        return aboutPage;

        //return inflater.inflate(R.layout.fragment_about_us_dialog, container, false);
    }
}
